package com.ezbuy.authservice.service.impl;

import com.ezbuy.authmodel.model.TenantIdentify;
import com.ezbuy.authservice.client.NotiServiceClient;
import com.ezbuy.authmodel.constants.AuthConstants;
import com.ezbuy.authservice.repository.OrganizationRepo;
import com.ezbuy.authservice.repository.TenantIdentifyRepo;
import com.ezbuy.authservice.service.IdentifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentifyServiceImpl implements IdentifyService {

    private final TenantIdentifyRepo tenantIdentifyRepo;
    private final NotiServiceClient notiServiceClient;
    private final OrganizationRepo organizationRepo;

    @Value("${minio.private.bucket}")
    public String smePrivateBucket;

    @Override
    public Mono<Boolean> existedTrustedOrgIdentify(List<TenantIdentify> identifies) {
        List<Mono<Boolean>> observers = new ArrayList<>();
        for (TenantIdentify identify : identifies) {
            var checkExisted = tenantIdentifyRepo.existsTrustedByIdNoAndType(
                    identify.getIdNo(), identify.getIdType(), AuthConstants.TenantType.ORGANIZATION);
            observers.add(checkExisted);
        }
        return Flux.concat(observers).collectList().flatMap(checkers -> {
            for (Boolean check : checkers) {
                if (Boolean.TRUE.equals(check)) {
                    return Mono.justOrEmpty(true);
                }
            }
            return Mono.justOrEmpty(false);
        });
    }
}
