package com.ezbuy.paymentservice.repository;

import com.viettel.sme.ordermodel.dto.OrderSyncDTO;
import com.viettel.sme.ordermodel.model.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OrderRepository extends R2dbcRepository<Order, UUID> {

    @Query(value = "select o.id, o.order_code, o.create_at as createAt, '1' as type, o.id as order_id from `order` o" +
            " where (:startTime is null or o.create_at >= :startTime)" +
            " and (:endTime is null or o.create_at < :endTime)" +
            " and o.state = :state" +
            " and o.status = 1" +
            " union all" +
            " select oi.id, oi.order_code, oi.create_at as createAt, '0' as type, oi.order_id as order_id from `order_item` oi" +
            " where (:startTime is null or oi.create_at >= :startTime)" +
            " and (:endTime is null or oi.create_at < :endTime)" +
            " and oi.state = :state" +
            " and oi.status = 1" +
            " order by createAt" +
            " limit :limit offset :offset")
    Flux<OrderSyncDTO> findAllOrderByStateAndTime(int state, LocalDateTime startTime, LocalDateTime endTime, int limit, long offset);

    @Query(value = "select count(o.id) from `order` o" +
            " where o.customer_id = :customerId" +
            " and (:state is null or o.state = :state)" +
            " and o.status = 1")
    Mono<Integer> countOrderHistory(String customerId, Integer state);

    @Query(value = "insert into sme_order.order " +
            "(id, order_code, customer_id, total_fee," +
            " currency, area_code, province, district, precinct, state, type," +
            " status, create_at, create_by, update_at, update_by," +
            " detail_address, id_no, name, email, phone)" +
            "values (:id, :orderCode, :customerId, :totalFee," +
            "        :currency, :areaCode, :province, :district," +
            "        :precinct, :state, :type, 1, NOW(), :createBy, NOW()," +
            "        :updateBy, :detailAddress, :idNo, :name, :email, :phone)")
    Mono<Order> insertOrder(String id, String orderCode, String customerId, Double totalFee, String currency,
                            String areaCode, String province, String district, String precinct, int state, String type,
                            String createBy, String updateBy, String detailAddress, String idNo, String name, String email, String phone);

    @Query(value = "insert into `order` " +
            "(id, customer_id, total_fee, " +
            " currency, state, type, " +
            " status, create_at, create_by, update_at, update_by, id_no, name, email, phone, province," +
            " district, precinct) " +
            " values (:id, :customerId, :totalFee, " +
            " :currency,:state, :type, 1, NOW(), :createBy, NOW(), :updateBy, :idNo, :name, :email, :phone," +
            " :province, :district, :precinct)")
    Mono<Order> insertOrderForPaidOrder(String id, String customerId, Double totalFee, String currency, int state, String type,
                                        String createBy, String updateBy, String idNo, String name, String email, String phone,
                                        String province, String district, String precinct);


    @Query(value = "select exists(id) from `order` ")
    Mono<Boolean> updateState2(Integer state, String updateBy, String orderId);

    @Query(value = "update `order` set state = :state, update_at = NOW(), update_by = :updateBy" +
            " where id = :orderId and status = 1")
    Mono<Void> updateState(Integer state, String updateBy, String orderId);
}
