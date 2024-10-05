package com.ezbuy.orderservice.service.impl;

import com.ezbuy.sme.framework.constants.CommonErrorCode;
import com.ezbuy.sme.framework.exception.BusinessException;
import com.ezbuy.sme.framework.factory.ObjectMapperFactory;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.framework.utils.DataUtil;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.ordermodel.dto.OrderFieldConfigDTO;
import com.ezbuy.sme.ordermodel.dto.request.GetOrderFieldConfigRequest;
import com.ezbuy.sme.ordermodel.model.OrderFieldConfig;
import com.ezbuy.orderservice.repository.OrderFieldConfigRepository;
import com.ezbuy.orderservice.service.OrderFieldConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderFieldConfigServiceImpl implements OrderFieldConfigService {

    private final OrderFieldConfigRepository orderFieldConfigRepository;

    @Override
    public Mono<DataResponse<OrderFieldConfigDTO>> getConfigByServiceTypeAndOrderType(GetOrderFieldConfigRequest request) {
        if (DataUtil.isNullOrEmpty(request.getLstTelecomServiceAlias())
                || DataUtil.isNullOrEmpty(request.getOrderType())) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "params.invalid"));
        }
        String orderType = request.getOrderType().trim();


        return orderFieldConfigRepository.findByOrderTypeAndTelecomServiceIds(orderType, request.getLstTelecomServiceAlias()).collectList() // sua originalId thanh alias
                .flatMap(configs -> {
                    if (DataUtil.isNullOrEmpty(configs)) {
                        return Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "config.not.found"));
                    }

                    Map<String, String> rsConfigMap = new HashMap<>();
                    for (OrderFieldConfig config : configs) {
                        Map<String, String> configMap = ObjectMapperFactory.getInstance().convertValue(config, Map.class);
                        for (Map.Entry<String, String> entry : configMap.entrySet()) {
                            String key = entry.getKey();
                            Set<String> uncheckPropertySet = Set.of("id", "status",
                                    "createTime", "createBy", "updateTime", "updateBy");
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
                    return Mono.just(new DataResponse<>("", Translator.toLocale("success"), rsConfig));
                });
    }
}
