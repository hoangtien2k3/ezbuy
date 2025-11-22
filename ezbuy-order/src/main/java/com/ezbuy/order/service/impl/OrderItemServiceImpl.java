package com.ezbuy.order.service.impl;

import com.ezbuy.order.dto.request.ReviewOrderItemRequest;
import com.ezbuy.order.repository.OrderItemRepository;
import com.ezbuy.order.service.OrderItemService;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.constants.Constants;
import com.ezbuy.core.constants.MessageConstant;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public Mono<DataResponse> review(ReviewOrderItemRequest request) {
        if (DataUtil.isNullOrEmpty(request.getOrderItemId())) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "order.item.required"));
        }
        String orderId = request.getOrderItemId().trim();
        if (!ValidateUtils.validateUUID(orderId)) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "order.item.invalid"));
        }
        if (DataUtil.isNullOrEmpty(request.getContent())) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "review.content.required"));
        }
        String content = request.getContent().trim();
        if (content.length() > 2000) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "review.content.invalid"));
        }

        return SecurityUtils.getCurrentUser()
                .flatMap(user -> Mono.zip(checkExistItem(orderId, user.getId()), Mono.just(user.getUsername())))
                .flatMap(existGroupData -> {
                    if (!existGroupData.getT1()) {
                        return Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "order.item.not.found"));
                    }
                    var updateMono = orderItemRepository.updateContent(orderId, content, existGroupData.getT2());
                    return AppUtils.insertData(updateMono);
                })
                .flatMap(rs -> {
                    if (!rs) {
                        return Mono.error(new RuntimeException());
                    }
                    return Mono.just(new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), null));
                });
    }

    private Mono<Boolean> checkExistItem(String orderItem, String userId) {
        return orderItemRepository
                .checkOrderItemExist(orderItem, userId)
                .map(rs -> DataUtil.safeEqual(rs, Constants.Activation.ACTIVE))
                .switchIfEmpty(Mono.just(false));
    }
}
