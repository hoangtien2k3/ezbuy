package com.ezbuy.order.service.impl;

import com.ezbuy.order.constains.Constants;
import com.ezbuy.order.constains.TemplateConstants;
import com.ezbuy.order.dto.OrderDetailDTO;
import com.ezbuy.order.dto.PaginationDTO;
import com.ezbuy.order.dto.request.SearchOrderRequest;
import com.ezbuy.order.dto.response.SearchOrderHistoryResponse;
import com.ezbuy.order.repoTemplate.OrderRepositoryTemplate;
import com.ezbuy.order.repository.*;
import com.ezbuy.order.service.OrderService;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static com.ezbuy.core.constants.MessageConstant.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositoryTemplate orderRepositoryTemplate;

    @Override
    public Mono<DataResponse> searchOrder(SearchOrderRequest request) {
        int size = DataUtil.safeToInt(request.getPageSize(), 20);
        if (size <= 0 || size > 100) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "size.invalid"));
        }

        int page = DataUtil.safeToInt(request.getPageIndex(), 1);
        if (page <= 0) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "page.invalid"));
        }

        Integer state = Constants.ORDER_STATE_MAP.get(DataUtil.safeTrim(request.getState()));
        String sort = DataUtil.safeToString(request.getSort(), "-createAt");

        return SecurityUtils.getCurrentUser().flatMap(tokenUser -> {
                    var countMono = orderRepository.countOrderHistory(tokenUser.getId(), state);
                    return Mono.zip(countMono, Mono.just(tokenUser.getId()));
                }).flatMap(countOrderGroupData -> {
                    long count = DataUtil.safeToLong(countOrderGroupData.getT1());
                    if (count == 0) {
                        return Mono.just(new DataResponse<>(SUCCESS, new SearchOrderHistoryResponse(page, size)));
                    }

                    int offset = PageUtils.getOffset(page, size);
                    var orderListMono = orderRepositoryTemplate
                            .searchOrderDetail(countOrderGroupData.getT2(), state, offset, size, sort)
                            .collectList();

                    return Mono.zip(orderListMono, Mono.just(count)).flatMap(orderGroupData -> {
                        Map<String, OrderDetailDTO> orderMap = new LinkedHashMap<>();
                        for (OrderDetailDTO order : orderGroupData.getT1()) {
                            if (orderMap.containsKey(order.getId())) {
                                orderMap.get(order.getId()).addItem(order.getItemList());
                            } else {
                                orderMap.put(order.getId(), order);
                            }
                        }

                        PaginationDTO pagination = PaginationDTO.builder()
                                .pageIndex(page)
                                .pageSize(size)
                                .totalRecords(count)
                                .build();

                        SearchOrderHistoryResponse response = SearchOrderHistoryResponse.builder()
                                .data(new ArrayList<>(orderMap.values()))
                                .pagination(pagination)
                                .build();
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), response));
                    });
                });
    }

    @Override
    public Mono<DataResponse> findDetail(String orderId) {
        String trimOrderId = DataUtil.safeTrim(orderId);
        if (!ValidateUtils.validateUUID(trimOrderId)) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "orderId.invalid"));
        }

        return SecurityUtils.getCurrentUser()
                .flatMap(tokenUser -> orderRepositoryTemplate
                        .findOneDetailById(tokenUser.getId(), trimOrderId)
                        .collectList())
                .flatMap(orderList -> {
                    if (DataUtil.isNullOrEmpty(orderList)) {
                        return Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "order.not.found"));
                    }

                    OrderDetailDTO orderDetail = orderList.getFirst();
                    for (int i = 1; i < orderList.size(); i++) {
                        orderDetail.addItem(orderList.get(i).getItemList());
                    }
                    return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), orderDetail));
                });
    }

    @Override
    public Mono<ResponseEntity<Resource>> getImportGroupMemberTemplate() {
        try {
            Resource resource = new ClassPathResource(TemplateConstants.IMPORT_GROUP_MEMBER_TEMPLATE_PATH);
            InputStream inputStream = resource.getInputStream();
            byte[] fileData = inputStream.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", TemplateConstants.IMPORT_GROUP_MEMBER_TEMPLATE_NAME);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return Mono.just(ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileData.length)
                    .body(resource));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }
}
