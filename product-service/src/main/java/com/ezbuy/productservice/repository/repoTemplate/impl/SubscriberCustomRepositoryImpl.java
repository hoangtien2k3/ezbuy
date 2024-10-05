package com.ezbuy.productservice.repository.repoTemplate.impl;

import com.ezbuy.productservice.repository.repoTemplate.SubscriberCustomRepository;
import com.ezbuy.sme.framework.utils.DataUtil;
import com.ezbuy.sme.productmodel.response.StatisticSubscriberResponse;
import com.ezbuy.sme.productmodel.response.TotalSubscriberResponse;
import lombok.Data;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@Repository
public class SubscriberCustomRepositoryImpl implements SubscriberCustomRepository {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<TotalSubscriberResponse> findByTelecomServiceIdAndIdNo(Long telecomServiceId) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("COUNT(1) as totalSubscriber, ");
        sqlBuilder.append("SUM(CASE WHEN s.expired_date < NOW() + INTERVAL 3 MONTH THEN 1 ELSE 0 END) as totalAboutExpiredSubscriber, ");
        sqlBuilder.append("COUNT(CASE WHEN s.expired_date < NOW() THEN 1 else 0 END) as totalExpiredSubscriber ");
        sqlBuilder.append("FROM sme_product.subcriber s WHERE s.telecom_service = :telecomServiceId ");
//        sqlBuilder.append("AND s.id_no = :idNo ");
// group by telecomServiceId
        return template.getDatabaseClient()
                .sql(sqlBuilder.toString())
                .bind("telecomServiceId", telecomServiceId)
//                .bind(idNo, idno);
                .map((row) -> new TotalSubscriberResponse(
                        row.get("totalSubscriber", Long.class),
                        row.get("totalAboutExpiredSubscriber", Long.class),
                        row.get("totalExpiredSubscriber", Long.class)))
                .one();
    }

    @Override
    public Flux<StatisticSubscriberResponse> getStatisticSubscriber(String idNo, List<String> lstTelecomAlias, Integer time) {

        return template.getDatabaseClient()
                .sql("select telecom_service_id as telecomServiceId, telecom_service_alias as telecomServiceAlias, COUNT(1) as totalSubscriber," +
                        " sum(case when status = 1 and expired_date < CURDATE() then 1 else 0 end) as totalExpiredSubscriber," +
                        " sum(case when status = 2 and expired_date <= CURDATE() + INTERVAL :time DAY then 1 else 0 end) as totalAboutExpiredSubscriber" +
                        " from subscriber s where id_no = :idNo and telecom_service_alias in (:lstTelecomAlias)" +
                        " group by telecom_service_id")
                .bind("idNo", idNo)
                .bind("lstTelecomAlias", lstTelecomAlias)
                .bind("time", time != null ? time : 30)
                .map((row) -> StatisticSubscriberResponse.builder()
                        .telecomServiceId(DataUtil.safeToLong(row.get("telecomServiceId")))
                        .telecomServiceAlias(DataUtil.safeToString(row.get("telecomServiceAlias")))
                        .totalSubscriber(DataUtil.safeToLong(row.get("totalSubscriber")))
                        .totalExpiredSubscriber(DataUtil.safeToLong(row.get("totalExpiredSubscriber")))
                        .totalAboutExpiredSubscriber(DataUtil.safeToLong(row.get("totalAboutExpiredSubscriber")))
                        .build())
                .all();
    }
}
