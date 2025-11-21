package com.ezbuy.paymentservice.service.impl;

import com.ezbuy.paymentservice.model.dto.GetOrderFieldConfigRequest;
import com.ezbuy.paymentservice.model.dto.OrderFieldConfig;
import com.ezbuy.paymentservice.model.dto.OrderFieldConfigDTO;
import com.ezbuy.paymentservice.repository.OrderFieldConfigRepository;
import com.ezbuy.paymentservice.service.OrderFieldConfigService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.factory.ObjectMapperFactory;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderFieldConfigServiceImpl implements OrderFieldConfigService {

    private final OrderFieldConfigRepository orderFieldConfigRepository;

    @Override
    public Mono<DataResponse<OrderFieldConfigDTO>> getConfigByServiceTypeAndOrderType(
            GetOrderFieldConfigRequest request) {
        if (DataUtil.isNullOrEmpty(request.getLstTelecomServiceAlias())
                || DataUtil.isNullOrEmpty(request.getOrderType())) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "params.invalid"));
        }
        String orderType = request.getOrderType().trim();
        return orderFieldConfigRepository
                .findByOrderTypeAndTelecomServiceIds(orderType, request.getLstTelecomServiceAlias())
                .collectList()
                .flatMap(configs -> {
                    if (DataUtil.isNullOrEmpty(configs)) {
                        return Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "config.not.found"));
                    }
                    Map<String, String> rsConfigMap = new HashMap<>();
                    for (OrderFieldConfig config : configs) {
                        @SuppressWarnings("unchecked")
                        Map<String, String> configMap = (Map<String, String>) ObjectMapperFactory.getInstance().convertValue(config, Map.class);
                        for (Map.Entry<String, String> entry : configMap.entrySet()) {
                            String key = entry.getKey();
                            Set<String> uncheckPropertySet = Set.of("id", "status", "createTime", "createBy", "updateTime", "updateBy");
                            if (uncheckPropertySet.contains(key)) {
                                continue;
                            }
                            String value = DataUtil.safeToString(entry.getValue());
                            if (!rsConfigMap.containsKey(key)) {
                                rsConfigMap.put(key, value);
                                continue;
                            }
                            int currentValue = DataUtil.safeToInt(rsConfigMap.get(key));
                            int newValue = DataUtil.safeToInt(value);
                            if (newValue > currentValue) {
                                rsConfigMap.replace(key, DataUtil.safeToString(newValue));
                            }
                        }
                    }
                    OrderFieldConfigDTO rsConfig = ObjectMapperFactory.getInstance().convertValue(rsConfigMap, OrderFieldConfigDTO.class);
                    return Mono.just(new DataResponse<>("","success", rsConfig));
                });
    }
}
