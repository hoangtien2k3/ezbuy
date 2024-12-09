package com.ezbuy.notificationsend.repository;

import com.ezbuy.notificationmodel.model.SendSms;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SendSmsRepository extends R2dbcRepository<SendSms, String> {

    @Query("SELECT sm.id, sm.isdn, sm.content, sm.status, sm.error, sm.create_date," +
            " sm.bp_id, sm.alias, sm.cp_code, sm.exchange_id" +
            " FROM send_sms sm" +
            " WHERE sm.status = :status" +
            "   AND sm.alias IS NOT NULL" +
            "   AND sm.create_date > CURRENT_DATE - CAST(:backDay AS INT)" +
            "   AND (:lock = FALSE OR EXISTS (" +
            "       SELECT 1 FROM send_sms sm_lock" +
            "       WHERE sm_lock.status = :status LIMIT 1" +
            "   ))" +
            " LIMIT :batchSize")
    Flux<SendSms> findByStatusAlias(Long status, Integer batchSize, Integer backDay, boolean lock);

    @Query("UPDATE send_sms" +
            " SET status = :status, error = :error, exchange_id = :exchangeId" +
            " WHERE id = :id")
    Mono<Boolean> updateResult(String id, Long status, String error, String exchangeId);

}



