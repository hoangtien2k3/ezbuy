package com.ezbuy.productservice.utils;

import com.ezbuy.productmodel.model.Subscriber;
import com.ezbuy.productmodel.response.SubscriberResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public class DataUtils {
    // Convert Mono<T> to Mono<Optional<T>>
    public static <T> Mono<Optional<T>> optional(Mono<T> mono) {
        return mono.map(Optional::of).defaultIfEmpty(Optional.empty());
    }

    public static Subscriber dtoToModel(SubscriberResponse subscriberResponse) {
        return Subscriber.builder()
                .id(subscriberResponse.getSubscriberId())
                .idNo(subscriberResponse.getIdNo())
                .isdn(subscriberResponse.getIsdn())
                .address(subscriberResponse.getAddress())
                .productId(subscriberResponse.getProductId())
                .productCode(subscriberResponse.getProductCode())
                .productName(subscriberResponse.getProductName())
                .activationDate(subscriberResponse.getActivationDate())
                .expiredDate(subscriberResponse.getExpiredDate())
                .status(subscriberResponse.getStatus())
                .groupType(subscriberResponse.getGroupType())
                .accountId(subscriberResponse.getAccountId())
                .telecomServiceId(subscriberResponse.getTelecomServiceId())
                .telecomServiceAlias(subscriberResponse.getTelecomServiceAlias())
                .isNew(true)
                .build();
    }
}
