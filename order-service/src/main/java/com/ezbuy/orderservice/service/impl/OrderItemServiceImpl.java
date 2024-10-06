package com.ezbuy.orderservice.service.impl;

import com.ezbuy.ordermodel.dto.request.ReviewOrderItemRequest;
import com.ezbuy.orderservice.repository.OrderItemRepository;
import com.ezbuy.orderservice.service.OrderItemService;
import io.hoangtien2k3.reactify.AppUtils;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SecurityUtils;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.constants.Constants;
import io.hoangtien2k3.reactify.constants.MessageConstant;
import io.hoangtien2k3.reactify.exception.BusinessException;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import io.hoangtien2k3.reactify.ValidateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public Mono<DataResponse> review(ReviewOrderItemRequest request) {
        if (DataUtil.isNullOrEmpty(request.getOrderItemId())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.item.required"));
        }
        String orderId = request.getOrderItemId().trim();
        if (!ValidateUtils.validateUUID(orderId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.item.invalid"));
        }

        if (DataUtil.isNullOrEmpty(request.getContent())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "review.content.required"));
        }

        String content = request.getContent().trim();
        if (content.length() > 2000) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "review.content.invalid"));
        }

        return SecurityUtils.getCurrentUser()
                .flatMap(user -> Mono.zip(
                        checkExistItem(orderId, user.getId()),
                        Mono.just(user.getUsername())
                ))
                .flatMap(existGroupData -> {
                    if (!existGroupData.getT1()) {
                        return Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "order.item.not.found"));
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
        return orderItemRepository.checkOrderItemExist(orderItem, userId)
                .map(rs -> DataUtil.safeEqual(rs, Constants.Activation.ACTIVE))
                .switchIfEmpty(Mono.just(false));
    }
}
