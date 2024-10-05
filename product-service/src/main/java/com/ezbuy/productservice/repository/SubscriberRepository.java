package com.ezbuy.productservice.repository;

import com.ezbuy.sme.productmodel.model.Subscriber;
import com.ezbuy.productservice.repository.repoTemplate.SubscriberCustomRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface SubscriberRepository extends R2dbcRepository<Subscriber, String>, SubscriberCustomRepository {

    @Query(value = "SELECT * FROM subcriber s WHERE s.telecom_service = :id")
    Flux<Subscriber> findByTelecomServiceId(Long id);

    @Query(value = "SELECT * FROM subscriber s WHERE s.id = :id")
    Mono<Subscriber> findById(String id);

    @Query(value = "SELECT * FROM subscriber s WHERE s.id_no = :idNo and (telecom_service_id = :telecomServiceId or :telecomServiceId is null) and (telecom_service_alias = :telecomServiceAlias or :telecomServiceAlias is null) order by expired_date asc, status desc")
    Flux<Subscriber> findByIdnoAndTelecomServiceIdAndTelecomServiceAlias(String idNo, Long telecomServiceId,String telecomServiceAlias);


    /***
     * Ham lay danh sach thue theo idno va dich vu trang thai hoat dong = 2 va ngay het han lon hon ngay hien tai
     * @param idNo so giay to
     * @param telecomServiceIds danh sach dich vu
     * @return
     */
    @Query(value = "SELECT * FROM subscriber s WHERE s.id_no = :idNo and telecom_service_id  in (:telecomServiceIds) and status = 2 and (expired_date is null or expired_date > now()) order by expired_date asc ")
    Flux<Subscriber> findByIdNoAndTelecomServiceIdIn(String idNo, List<String> telecomServiceIds);

    @Query(value = "SELECT * FROM subscriber s WHERE s.isdn = :isdn")
    Mono<Subscriber> findByIsdn(String isdn);

    @Query(value = "select *\n" +
            "from subscriber\n" +
            "where status = 2\n" +
            "and id_no in (:idNoList)")
    Flux<Subscriber> getAllRegisterdTelecomServicesByIdNoList(List<String> idNoList);

    @Query(value = "select distinct(telecom_service_id) from subscriber where status =2 and id_no in (:idNos)")
    Flux<String> findTelecomServicesByIdNo(List<String> idNos);

    @Query(value = "select telecom_service_alias\n" +
            "from subscriber\n" +
            "where status = 2\n" +
            "and id_no in (:idNoList)")
    Flux<Subscriber> getAllRegisterdTelecomServicesAliasByIdNoList(List<String> idNoList);

    @Query(value = "SELECT * FROM subscriber s WHERE s.id_no = :idNo and telecom_service_alias = :telecomServiceAlias order by expired_date asc, status desc")
    Flux<Subscriber> findByIdnoAndTelecomServiceAlias(String idNo, String telecomServiceAlias);

    @Query(value = "select distinct(telecom_service_alias) from subscriber where status =2 and id_no in (:idNos)")
    Flux<String> findTelecomAlilasByIdNo(List<String> idNos);

    @Query(value = "SELECT * FROM subscriber s WHERE s.id_no = :idNo and telecom_service_alias  in (:telecomServiceAlias) and status = 2 and (expired_date is null or expired_date > now()) order by expired_date asc ")
    Flux<Subscriber> findByIdNoAndTelecomServiceAliasIn(String idNo, List<String> telecomServiceAlias);


    @Query(value = "SELECT * FROM subscriber s WHERE s.id_no = :idNo and  telecom_service_id in (:telecomServiceIds)) " +
            "and telecom_service_alias  in (:telecomServiceAliass) and status = 2 and (expired_date is null or expired_date > now()) order by expired_date asc ")
    Flux<Subscriber> findByIdNoAndTelecomServiceIdAndAliasIn(String idNo, List<String> telecomServiceIds, List<String> telecomServiceAliass);

    @Query(value = "select distinct(telecom_service_alias) from subscriber where status =2 and id_no in (:idNos)")
    Flux<String> findTelecomAliasByIdNo(List<String> idNos);

    @Query(value = "SELECT * FROM subscriber s WHERE s.id_no in (:idNo) and telecom_service_alias  in (:telecomServiceAlias)\n" +
            "                             and status = 2 and (expired_date is null or expired_date > now()) order by expired_date asc")
    Flux<Subscriber> findByIdNoInAndTelecomServiceAliasIn(List<String> idNo, List<String> telecomServiceAlias);
}
