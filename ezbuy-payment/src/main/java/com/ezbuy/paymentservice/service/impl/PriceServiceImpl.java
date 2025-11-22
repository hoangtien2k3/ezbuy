package com.ezbuy.paymentservice.service.impl;

import com.ezbuy.paymentservice.client.ProductClient;
import com.ezbuy.paymentservice.model.dto.request.ProductItem;
import com.ezbuy.paymentservice.model.dto.request.ProductPriceRequest;
import com.ezbuy.paymentservice.model.dto.response.ProductPrice;
import com.ezbuy.paymentservice.service.PriceService;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.util.DataUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final ProductClient productClient;

    @Override
    public Mono<ProductPrice> calculatePrices(ProductPriceRequest productPriceRequest) {
        long totalPrice = 0L;
        List<ProductItem> productItems = productPriceRequest.getProductItems();
        if (productItems == null || productItems.isEmpty()) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "product.item.null"));
        }
        Map<String, Integer> missingPriceProductIdsMap = new HashMap<>();
        for (ProductItem productItem : productItems) {
            String templateId = productItem.getTemplateId();
            Long price = productItem.getPrice();
            Integer quantity = productItem.getQuantity();
            if (DataUtil.isNullOrEmpty(templateId)) {
                return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "product.item.id.null"));
            }
            if (quantity == null || quantity < 1) {
                return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "product.item.quantity.invalid"));
            }
            if (price == null) {
                missingPriceProductIdsMap.merge(templateId, quantity, Integer::sum);
            } else if (price < 0) {
                return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "product.item.price.invalid"));
            } else {
                totalPrice += price * quantity;
            }
        }
        if (!missingPriceProductIdsMap.isEmpty()) {
            return calculateWithMissingPriceProducts(missingPriceProductIdsMap, totalPrice);
        } else {
            return Mono.just(new ProductPrice(totalPrice));
        }
    }

    private Mono<ProductPrice> calculateWithMissingPriceProducts(
            Map<String, Integer> missingPriceProductIdsMap,
            Long currentTotalPrice) {
        return productClient
                .getExProductPrices(missingPriceProductIdsMap.keySet())
                .flatMap(rs -> {
                    Long productPrice = rs.getPrice();
                    Integer quantity = missingPriceProductIdsMap.get(rs.getTemplateId());
                    if (productPrice == null) {
                        return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "call.product.service.error"));
                    }
                    return Mono.just(productPrice * quantity);
                })
                .reduce(currentTotalPrice, Long::sum)
                .flatMap(rs -> Mono.just(new ProductPrice(rs)));
    }
}
