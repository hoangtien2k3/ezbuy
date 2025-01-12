package com.ezbuy.productservice.repository.repoTemplate.impl;

import com.ezbuy.productmodel.dto.response.StatisticSubscriberResponse;
import com.ezbuy.productservice.repository.repoTemplate.SubscriberCustomRepo;
import com.reactify.util.DataUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class SubscriberCustomRepoImpl implements SubscriberCustomRepo {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<StatisticSubscriberResponse> getStatisticSubscriber(String idNo, List<Long> telecomServiceIds) {

        return template.getDatabaseClient()
                .sql("select telecom_service_id as telecomServiceId, COUNT(1) as totalSubscriber,"
                        + " sum(case when status = 1 and expired_date < CURDATE() then 1 else 0 end) as totalExpiredSubscriber,"
                        + " sum(case when status = 2 and expired_date <= CURDATE() + INTERVAL 30 DAY then 1 else 0 end) as totalAboutExpiredSubscriber"
                        + " from subscriber s where id_no = :idNo and telecom_service_id in (:telecomServiceIds)"
                        + " group by telecom_service_id")
                .bind("idNo", idNo)
                .bind("telecomServiceIds", telecomServiceIds)
                .map((row) -> StatisticSubscriberResponse.builder()
                        .telecomServiceId(DataUtil.safeToLong(row.get("telecomServiceId")))
                        .totalSubscriber(DataUtil.safeToLong(row.get("totalSubscriber")))
                        .totalExpiredSubscriber(DataUtil.safeToLong(row.get("totalExpiredSubscriber")))
                        .totalAboutExpiredSubscriber(DataUtil.safeToLong(row.get("totalAboutExpiredSubscriber")))
                        .build())
                .all();
    }
}
