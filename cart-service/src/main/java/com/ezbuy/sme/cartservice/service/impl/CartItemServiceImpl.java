package com.ezbuy.sme.cartservice.service.impl;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;
import static com.reactify.constants.MessageConstant.QUERY_CART_ITEM_NOT_FOUND;
import static com.reactify.constants.Regex.PRODUCT_ID;

import com.ezbuy.cartmodel.dto.CartItemProductDTO;
import com.ezbuy.cartmodel.dto.CartTelecomDTO;
import com.ezbuy.cartmodel.dto.DeleteUserProductCartDTO;
import com.ezbuy.cartmodel.dto.request.Product;
import com.ezbuy.cartmodel.dto.response.PageCart;
import com.ezbuy.cartmodel.model.Cart;
import com.ezbuy.cartmodel.model.CartItem;
import com.ezbuy.ordermodel.dto.PaginationDTO;
import com.ezbuy.paymentmodel.dto.request.ProductItem;
import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.productmodel.response.ProductOfferTemplateDTO;
import com.ezbuy.sme.cartservice.client.PaymentClient;
import com.ezbuy.sme.cartservice.client.ProductClient;
import com.ezbuy.sme.cartservice.client.SettingClient;
import com.ezbuy.sme.cartservice.repository.CartItemRepository;
import com.ezbuy.sme.cartservice.repository.CartRepository;
import com.ezbuy.sme.cartservice.service.CartItemService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.*;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final SettingClient settingClient;
    private final ProductClient productClient;
    private final R2dbcEntityTemplate template;
    private final PaymentClient paymentClient;

    @Override
    public Mono<DataResponse<CartItem>> deleteCartItem(String id) {
        String message = QUERY_CART_ITEM_NOT_FOUND;
        String messageSuccess = SUCCESS;
        String cartItemId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(cartItemId)) {
            return SecurityUtils.getCurrentUser().flatMap(tokenUser -> cartRepository
                    .findByUserId(tokenUser.getId())
                    .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, message)))
                    .flatMap(cart -> {
                        String cartId = String.valueOf(cart.getId());
                        return cartItemRepository
                                .findByCartId(cartId)
                                .flatMap(cartItem -> Mono.just(Optional.ofNullable(cartItem)))
                                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, message)))
                                .flatMap(deletes -> {
                                    cartItemRepository
                                            .deleteAllByCartId(cartId, tokenUser.getUsername())
                                            .subscribe();
                                    return Mono.just(
                                            new DataResponse<>(null, Translator.toLocaleVi(messageSuccess), null));
                                });
                    }));
        } else {
            return SecurityUtils.getCurrentUser().flatMap(tokenUser -> cartItemRepository
                    .findById(cartItemId)
                    .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, message)))
                    .flatMap(cartItem -> {
                        String cartId = cartItem.getCartId();

                        return cartRepository
                                .findById(cartId)
                                .flatMap(cart -> Mono.just(Optional.ofNullable(cart)))
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.cart.null")))
                                .flatMap(delete -> {
                                    cartItemRepository
                                            .deleteCartItem(cartItemId, tokenUser.getUsername())
                                            .subscribe();
                                    return Mono.just(
                                            new DataResponse<>(null, Translator.toLocaleVi(messageSuccess), null));
                                });
                    }));
        }
    }

    @Override
    public Mono<DataResponse<CartItem>> addCartItem(Product product) {
        listProductIdValidate(product);
        return SecurityUtils.getCurrentUser().flatMap(tokenUser -> cartRepository
                .findByUserIdFlux(tokenUser.getId())
                .collectList()
                .flatMap(cart -> {
                    UUID idCart = UUID.randomUUID();
                    // create cart if is null
                    if (DataUtil.isNullOrEmpty(cart)) {
                        return createIfCartByUserIsNull(
                                tokenUser.getId(), tokenUser.getUsername(), idCart.toString(), product);
                    }
                    // create cartItem
                    String cartId = cart.getFirst().getId();
                    return cartItemRepository
                            .findByCartIdAndProductId(cartId, product.getListProductId())
                            .collectList()
                            .flatMap(cartItems ->
                                    createCartItemIfExits(cartItems, product, cartId, tokenUser.getUsername()));
                }));
    }

    private Mono<DataResponse<CartItem>> createIfCartByUserIsNull(
            String userId, String user, String cartId, Product product) {
        List<CartItem> cartItemList = new ArrayList<>();
        Cart cartBuild = Cart.builder()
                .id(cartId)
                .userId(userId)
                .status(1)
                .createAt(null)
                .updateAt(null)
                .createBy(user)
                .updateBy(user)
                .build();
        return cartRepository.save(cartBuild).flatMap(saveCart -> {
            for (String p : product.getListProductId()) {
                if (DataUtil.isNullOrEmpty(p)) {
                    continue;
                }
                String productIdTrim = DataUtil.safeTrim(p);
                CartItem cartItem = buildCartItem(
                        productIdTrim, product.getTelecomServiceId(), product.getTelecomServiceAlias(), cartId, user);
                cartItemList.add(cartItem);
            }
            AppUtils.runHiddenStream(cartItemRepository.saveAll(cartItemList).collectList());
            return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null));
        });
    }

    private Mono<DataResponse<CartItem>> createCartItemIfExits(
            List<CartItem> cartItems, Product product, String cartId, String user) {
        List<String> listProductIdNew = new ArrayList<>();
        List<String> listProductIdOld = new ArrayList<>();
        List<String> listCartItemId = new ArrayList<>();
        List<CartItem> cartItemList = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            listProductIdOld.add(cartItem.getProductId());
            listCartItemId.add(cartItem.getId());
        }
        for (String id : product.getListProductId()) {
            String idTrim = DataUtil.safeTrim(id);
            if (DataUtil.isNullOrEmpty(idTrim)) {
                continue;
            }
            if (!listProductIdOld.contains(idTrim)) {
                listProductIdNew.add(idTrim);
            }
        }
        for (String id : listProductIdNew) {
            CartItem cartItemBuild =
                    buildCartItem(id, product.getTelecomServiceId(), product.getTelecomServiceAlias(), cartId, user);
            cartItemList.add(cartItemBuild);
        }
        AppUtils.runHiddenStream(cartItemRepository.saveAll(cartItemList).collectList());
        if (!DataUtil.isNullOrEmpty(listProductIdOld)) {
            AppUtils.runHiddenStream(cartItemRepository.updateForAdd(listCartItemId, user));
        }
        return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null));
    }

    private CartItem buildCartItem(
            String productId, String telecomServiceId, String telecomServiceAlias, String cartId, String user) {
        UUID id = UUID.randomUUID();
        return CartItem.builder()
                .id(id.toString())
                .productId(productId)
                .telecomServiceId(telecomServiceId)
                .serviceAlias(telecomServiceAlias)
                .cartId(cartId)
                .quantity(1L)
                .status(1)
                .createAt(null)
                .createBy(user)
                .updateAt(null)
                .updateBy(user)
                .build();
    }

    private void listProductIdValidate(Product product) {
        for (String validateProductId : product.getListProductId()) {
            if (!validateProductId.matches(PRODUCT_ID)) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "productId.regex");
            }
            if (validateProductId.length() > 255) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "productId.max.length");
            }
        }
        if (!product.getTelecomServiceId().matches(PRODUCT_ID)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "telecom.id.regex");
        }
        if (product.getTelecomServiceId().length() > 255) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "telecom.max.length");
        }
        if (product.getTelecomServiceAlias().length() > 255) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "telecom.alias.max.length");
        }
    }

    @Override
    public Mono<DataResponse<CartItem>> updateQuantity(String cartItemId, Long quantity) {
        String cartItemIdTrim = DataUtil.safeTrim(cartItemId);
        if (DataUtil.isNullOrEmpty(cartItemIdTrim)) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "cartItem.not.null"));
        }
        if (DataUtil.isNullOrEmpty(quantity)) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "quantity.not.null"));
        }
        if (!ValidateUtils.validateUUID(cartItemIdTrim)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.invalid.format"));
        }
        if (quantity <= 0L) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "quantity.more.0"));
        }
        if (quantity > 9223372036854775806L) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "long.max.length"));
        }
        return SecurityUtils.getCurrentUser()
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null")))
                .flatMap(tokenUser -> cartItemRepository
                        .findById(cartItemIdTrim)
                        .flatMap(cart -> Mono.just(Optional.ofNullable(cart)))
                        .switchIfEmpty(Mono.error(
                                new BusinessException(CommonErrorCode.NOT_FOUND, "query.cartItem.not.found")))
                        .flatMap(cartItem -> {
                            String id = cartItem.get().getId();
                            AppUtils.runHiddenStream(
                                    cartItemRepository.updateQuantity(id, quantity, tokenUser.getUsername()));

                            return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null));
                        }));
    }

    @Override
    public Mono<DataResponse<PageCart>> getListCartItem(Integer pageSize, Integer pageIndex) {
        if (pageIndex < 1) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.pageIndex.invalid")));
        }
        if (pageSize < 1) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.pageSize.invalid")));
        }
        StringBuilder query = new StringBuilder();
        query.append("SELECT b.*\n" + "FROM\n" + "    (\n"
                + "        SELECT telecom_service_id, max(create_at) create_at\n" + "        FROM cart_item\n"
                + "        GROUP BY telecom_service_id\n" + "        ORDER BY create_at\n" + "    ) a\n"
                + "LEFT JOIN cart_item b ON a.telecom_service_id = b.telecom_service_id\n"
                + "LEFT JOIN cart ON b.cart_id = cart.id\n" + "WHERE b.status = 1\n" + "AND cart.user_id = :userId\n"
                + "ORDER BY a.create_at desc,b.create_at desc\n");
        query.append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        BigDecimal index = (new BigDecimal(pageIndex - 1)).multiply(new BigDecimal(pageSize));
        return SecurityUtils.getCurrentUser().flatMap(tokenUser -> cartRepository
                .findByUserId(tokenUser.getId())
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.cart.not.found")))
                .flatMap(cart -> {
                    // get list cart_item
                    String cartId = cart.getId();
                    var cartItemList = template.getDatabaseClient()
                            .sql(String.valueOf(query))
                            .bind("userId", tokenUser.getId())
                            .bind("pageSize", pageSize)
                            .bind("index", index)
                            .map((row, rowMetadata) -> this.build(row))
                            .all()
                            .collectList();
                    // count total item in cart
                    var countItem = cartItemRepository.countQuantityItem(cartId);
                    // get all telecom service
                    var telecomServiceList = settingClient.getTelecomService();
                    return Mono.zip(cartItemList, countItem, telecomServiceList).flatMap(tuple -> {
                        // get list telecom
                        //
                        if (DataUtil.isNullOrEmpty(tuple.getT1()) || DataUtil.isNullOrEmpty(tuple.getT3())) {
                            PageCart pageCart = new PageCart();
                            pageCart.setTotalCount(tuple.getT2());
                            PaginationDTO pagination = new PaginationDTO();
                            pagination.setTotalRecords(tuple.getT2().longValue());
                            pagination.setPageIndex(pageIndex);
                            pagination.setPageSize(pageSize);
                            pageCart.setPagination(pagination);
                            return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), pageCart));
                        }
                        return cartItemRepository
                                .getTotalRecordsByUserId(tokenUser.getId())
                                .flatMap(totelRecords -> {
                                    PaginationDTO pagination = PaginationDTO.builder()
                                            .totalRecords(totelRecords)
                                            .pageIndex(pageIndex)
                                            .pageSize(pageSize)
                                            .build();
                                    return getListProductCartItemDTO(
                                            tuple.getT1(), tuple.getT3(), tuple.getT2(), pagination);
                                });
                    });
                }));
    }

    @Override
    public Mono<DataResponse<Object>> deleteListItem(DeleteUserProductCartDTO deleteUserProductCartDTO) {
        if (DataUtil.isNullOrEmpty(deleteUserProductCartDTO.getUserId())) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "user.input.notnull"));
        }
        if (!DataUtil.isUUID(DataUtil.safeTrim(deleteUserProductCartDTO.getUserId()))) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.invalid.format"));
        }
        if (DataUtil.isNullOrEmpty(deleteUserProductCartDTO.getListProductId())) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "list.productId.notnull"));
        }
        deleteUserProductCartDTO.setListProductId(deleteUserProductCartDTO.getListProductId().stream()
                .map(x -> DataUtil.safeTrim(x))
                .collect(Collectors.toList()));
        StringBuilder getList = new StringBuilder();
        getList.append("\'");
        getList.append(String.join("\',\'", deleteUserProductCartDTO.getListProductId()));
        StringBuilder query = new StringBuilder();
        query.append("UPDATE cart_item ci\n" + "INNER JOIN cart c on ci.cart_id = c.id\n"
                + "SET ci.status = 0,ci.update_by = 'system',ci.update_at = NOW()\n" + "WHERE c.user_id = :userId\n"
                + "AND ci.status = 1\n" + "AND c.status = 1\n" + "AND  ci.product_id IN ( ");
        query.append(getList);
        query.append(" \')");
        return template.getDatabaseClient()
                .sql(String.valueOf(query))
                .bind("userId", DataUtil.safeTrim(deleteUserProductCartDTO.getUserId()))
                .then()
                .then(Mono.defer(() -> Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null))))
                .switchIfEmpty(Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null)));
    }

    private Mono<DataResponse<PageCart>> getListProductCartItemDTO(
            List<CartItem> itemList,
            List<CartTelecomDTO> cartTelecomDTOList,
            Integer totalCount,
            PaginationDTO pagination) {
        // filter list productId
        List<String> productId = itemList.stream().map(CartItem::getProductId).collect(Collectors.toList());
        return productClient
                .getProductInfo(productId)
                .flatMap(productInfoData -> {
                    List<ProductOfferTemplateDTO> productInfoDetailList = productInfoData.getData();
                    if (DataUtil.isNullOrEmpty(productInfoDetailList)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("resource.not.found")));
                    }
                    Map<String, List<CartItemProductDTO>> mapByServiceId =
                            getMapCartItemProductDTOByTelecomService(productInfoDetailList, itemList);
                    List<CartTelecomDTO> rspList = mapByServiceId.entrySet().stream()
                            .map(entry -> {
                                CartTelecomDTO cartTelecomDTO = cartTelecomDTOList.stream()
                                        .filter(cart -> DataUtil.safeEqual(cart.getOriginId(), entry.getKey()))
                                        .findAny()
                                        .orElse(new CartTelecomDTO());
                                cartTelecomDTO.setProductCartItemList(entry.getValue());
                                return cartTelecomDTO;
                            })
                            .collect(Collectors.toList());
                    List<ProductItem> productItemList = new ArrayList<>();
                    for (CartTelecomDTO telecomDTO : cartTelecomDTOList) {
                        List<CartItemProductDTO> dtoList = mapByServiceId.get(telecomDTO.getOriginId());
                        if (!DataUtil.isNullOrEmpty(dtoList)) {
                            telecomDTO
                                    .getProductCartItemList()
                                    .forEach(x -> productItemList.add(new ProductItem(
                                            x.getProductId(), x.getTotalPrice(), Math.toIntExact(x.getQuantity()))));
                        }
                    }
                    ProductPriceRequest productPriceRequest = new ProductPriceRequest(productItemList);
                    return paymentClient
                            .estimatePrice(productPriceRequest)
                            .flatMap(totalPrice -> Mono.just(new DataResponse<>(
                                    Translator.toLocaleVi(SUCCESS),
                                    new PageCart(rspList, totalCount, totalPrice.orElse(0L), pagination))))
                            .switchIfEmpty(Mono.just(new DataResponse<>(
                                    Translator.toLocaleVi(SUCCESS),
                                    new PageCart(rspList, totalCount, 0L, pagination))));
                })
                .switchIfEmpty(Mono.just(new DataResponse<>(
                        CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("resource.not.found"), null)));
    }

    private Map<String, List<CartItemProductDTO>> getMapCartItemProductDTOByTelecomService(
            List<ProductOfferTemplateDTO> productOfferTemplateDTOList, List<CartItem> cartItemList) {
        Map<String, List<CartItemProductDTO>> map = new HashMap<>();
        for (ProductOfferTemplateDTO pro : productOfferTemplateDTOList) {
            if (!map.containsKey(pro.getTelecomServiceId())) {
                map.put(pro.getTelecomServiceId(), new ArrayList<>());
            }
            CartItem cartItem = cartItemList.stream()
                    .filter(x -> x.getProductId().equals(pro.getProductOfferTemplateId()))
                    .findFirst()
                    .orElse(new CartItem());
            CartItemProductDTO cartItemProductDTO = CartItemProductDTO.builder()
                    .productId(pro.getProductOfferTemplateId())
                    .templateCode(pro.getTemplateCode())
                    .templateName(pro.getTemplateName())
                    .imageUrl(pro.getImageLink())
                    .totalPrice(pro.getTotalPrice())
                    .itemId(cartItem.getId())
                    .cartId(cartItem.getCartId())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(DataUtil.safeToLong(pro.getCost(), pro.getTotalPrice()))
                    .discountPrice(DataUtil.safeToLong(pro.getCost(), pro.getTotalPrice()))
                    .durationValue(pro.getDurationValue())
                    .createAt(cartItem.getCreateAt())
                    .build();
            map.get(pro.getTelecomServiceId()).add(cartItemProductDTO);
        }
        for (Map.Entry<String, List<CartItemProductDTO>> entry : map.entrySet()) {
            entry.getValue().sort((o1, o2) -> o2.compareCreateAt(o1));
        }
        return map.entrySet().stream()
                .sorted((e1, e2) ->
                        e2.getValue().getFirst().compareCreateAt(e1.getValue().getFirst()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private CartItem build(Row row) {
        return CartItem.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .productId(DataUtil.safeToString(row.get("product_id")))
                .cartId(DataUtil.safeToString(row.get("cart_id")))
                .quantity(DataUtil.safeToLong(row.get("quantity")))
                .status(DataUtil.safeToInt(row.get("status")))
                .createAt((LocalDateTime) row.get("create_at"))
                .createBy(DataUtil.safeToString(row.get("create_by")))
                .updateAt((LocalDateTime) row.get("update_at"))
                .updateBy(DataUtil.safeToString(row.get("update_by")))
                .telecomServiceId(DataUtil.safeToString(row.get("telecom_service_id")))
                .build();
    }
}
