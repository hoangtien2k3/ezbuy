package com.ezbuy.paymentservice.service.impl;

import com.ezbuy.paymentmodel.dto.request.ProductItem;
import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.paymentmodel.dto.response.ProductPrice;
import com.ezbuy.paymentservice.client.ProductClient;
import com.ezbuy.paymentservice.service.PriceService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.util.DataUtil;
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
        Long totalPrice = 0L;
        List<ProductItem> productItems = productPriceRequest.getProductItems();
        if (productItems == null || productItems.isEmpty()) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.item.null"));
        }
        Map<String, Integer> missingPriceProductIdsMap = new HashMap<>();
        for (ProductItem productItem : productItems) {
            String templateId = productItem.getTemplateId();
            Long price = productItem.getPrice();
            Integer quantity = productItem.getQuantity();
            if (DataUtil.isNullOrEmpty(templateId)) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.item.id.null"));
            }
            if (quantity == null || quantity < 1) {
                return Mono.error(
                        new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.item.quantity.invalid"));
            }
            if (price == null) {
                Integer existQuantity = missingPriceProductIdsMap.get(templateId);
                if (existQuantity != null) {
                    missingPriceProductIdsMap.put(templateId, existQuantity + quantity);
                } else {
                    missingPriceProductIdsMap.put(templateId, quantity);
                }
            } else if (price < 0) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.item.price.invalid"));
            } else {
                totalPrice += price * quantity;
            }
        }
        if (missingPriceProductIdsMap.size() > 0) {
            return calculateWithMissingPriceProducts(missingPriceProductIdsMap, totalPrice);
        } else {
            return Mono.just(new ProductPrice(totalPrice));
        }
    }

    private Mono<ProductPrice> calculateWithMissingPriceProducts(
            Map<String, Integer> missingPriceProductIdsMap, Long currentTotalPrice) {
        return productClient
                .getExProductPrices(missingPriceProductIdsMap.keySet())
                .map(rs -> {
                    Long productPrice = rs.getPrice();
                    Integer quantity = missingPriceProductIdsMap.get(rs.getTemplateId());
                    if (productPrice == null) {
                        Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, "call.product.service.error"));
                    }
                    return productPrice * quantity;
                })
                .reduce(currentTotalPrice, Long::sum)
                .flatMap(rs -> Mono.just(new ProductPrice(rs)));
    }
}
