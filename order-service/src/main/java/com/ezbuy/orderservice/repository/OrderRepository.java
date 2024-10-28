package com.ezbuy.orderservice.repository;

import com.ezbuy.ordermodel.dto.OrderSyncDTO;
import com.ezbuy.ordermodel.dto.response.GetOrderReportResponse;
import com.ezbuy.ordermodel.model.Order;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository extends R2dbcRepository<Order, UUID> {

    @Query(
            value = "select o.id, o.order_code, o.create_at as createAt, '1' as type, o.id as order_id from `order` o"
                    + " where (:startTime is null or o.create_at >= :startTime)"
                    + " and (:endTime is null or o.create_at < :endTime)" + " and o.state = :state"
                    + " and o.status = 1"
                    + " union all"
                    + " select oi.id, oi.order_code, oi.create_at as createAt, '0' as type, oi.order_id as order_id from `order_item` oi"
                    + " where (:startTime is null or oi.create_at >= :startTime)"
                    + " and (:endTime is null or oi.create_at < :endTime)" + " and oi.state = :state"
                    + " and oi.status = 1"
                    + " order by createAt" + " limit :limit offset :offset")
    Flux<OrderSyncDTO> findAllOrderByStateAndTime(
            int state, LocalDateTime startTime, LocalDateTime endTime, int limit, long offset);

    @Query(
            value = "select count(*) " + "from (select `order`.id, `order`.state " + "      from `order` "
                    + "      where order_code is not null " + "        and customer_id = :customerId and status = 1 "
                    + "      union " + "      select order_item.id, order_item.state " + "      from order_item "
                    + "               left join sme_order.`order` o on o.id = order_item.order_id "
                    + "      where order_item.order_code is not null and o.status = 1 and order_item.status = 1 "
                    + "        and customer_id = :customerId) orderAll "
                    + "where (:state is null or orderAll.state = :state)")
    Mono<Integer> countOrderHistory(String customerId, Integer state);

    @Query(
            value = "insert into `order` " + "(id, order_code, customer_id, individual_id, total_fee, "
                    + " currency, area_code, province, district, precinct, state, type, "
                    + " status, create_at, create_by, update_at, update_by) "
                    + " values (:id, :orderCode, :customerId, :individualId, :totalFee, "
                    + " :currency, :areaCode, :provinceName, :districtName, "
                    + " :precinctName, :state, :type, 1, NOW(), :createBy, NOW(), :updateBy) ")
    Mono<Order> insertOrder(
            String id,
            String orderCode,
            String customerId,
            String individualId,
            Double totalFee,
            String currency,
            String areaCode,
            String provinceName,
            String districtName,
            String precinctName,
            int state,
            String type,
            String createBy,
            String updateBy);

    @Query(
            value = "insert into `order` " + "(id, customer_id, individual_id, total_fee, " + " currency, state, type, "
                    + " status, create_at, create_by, update_at, update_by, id_no, name, email, phone, province,"
                    + " district, precinct) " + " values (:id, :customerId, :individualId, :totalFee, "
                    + " :currency,:state, :type, 1, NOW(), :createBy, NOW(), :updateBy, :idNo, :name, :email, :phone,"
                    + " :province, :district, :precinct)")
    Mono<Order> insertOrderForPaidOrder(
            String id,
            String customerId,
            String individualId,
            Double totalFee,
            String currency,
            int state,
            String type,
            String createBy,
            String updateBy,
            String idNo,
            String name,
            String email,
            String phone,
            String province,
            String district,
            String precinct);

    @Query(value = "select exists(id) from `order` ")
    Mono<Boolean> updateState2(Integer state, String updateBy, String orderId);

    @Query(
            value = "update `order` set state = :state, update_at = NOW(), update_by = :updateBy"
                    + " where id = :orderId and status = 1")
    Mono<Void> updateState(Integer state, String updateBy, String orderId);

    @Modifying
    @Query(
            value =
                    "update `order` set state = :state, update_at = NOW(), update_by = :updateBy, order_code = :orderCode"
                            + " where id = :orderId and status = 1")
    Mono<Long> updateStateAndOrderCode(Integer state, String updateBy, String orderId, String orderCode);

    @Query(
            value =
                    "insert into `order` " + " (id, order_code, customer_id, individual_id, total_fee, "
                            + " currency, area_code, province, district, precinct, status, create_at, create_by, update_at, update_by,"
                            + " detail_address, state, description, type, id_no, name, email, phone) "
                            + " values (:id, :orderCode, :customerId, :individualId, :totalFee, "
                            + " :currency, :areaCode, :province, :district, "
                            + " :precinct, 1, NOW(), :createBy, NOW(), :updateBy, :detailAddress, :state, :description, :type, :idNo, :name, :email, :phone)")
    Mono<Order> insertOrderExt(
            String id,
            String orderCode,
            String customerId,
            String individualId,
            Double totalFee,
            String currency,
            String areaCode,
            String province,
            String district,
            String precinct,
            String createBy,
            String updateBy,
            String detailAddress,
            Integer state,
            String description,
            String type,
            String idNo,
            String name,
            String email,
            String phone);

    @Query(value = "select * from `order` o" + " where o.order_code = :orderCode")
    Flux<Order> findFirstByOrderCode(String orderCode);

    String insertQueryWithoutLogin = "insert into `order` "
            + "(id, order_code, total_fee, currency, state, type, status, create_at, create_by, update_at, update_by, name, email, phone, company_name) "
            + "values (:id, :orderCode, :totalFee, :currency, :state, :type, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM', :name, :email, :phone, :companyName)";

    @Query(insertQueryWithoutLogin)
    Mono<Order> insertOrderWithoutLogin(
            String id,
            String orderCode,
            Double totalFee,
            String currency,
            int state,
            String type,
            String name,
            String email,
            String phone,
            String companyName);

    @Query(value = "select * from `order` o" + " where o.status = 1 and o.order_code = :orderCode")
    Flux<Order> findByOrderCode(String orderCode);

    @Query(
            value = "update `order` set logs = :logs, update_at = NOW(), update_by = :updateBy"
                    + " where id = :orderId and status = 1")
    Mono<Void> updateLogs(String logs, String updateBy, String orderId);

    @Query(value = "select * from `order` o " + " where o.id = :orderId or o.order_code = :orderCode ")
    Flux<Order> findFirstByOrderIdAndOrderCode(String orderId, String orderCode);

    @Query(
            value = "select SUM(IF(od.state = 3, 1, 0)) success_order_count, "
                    + "       SUM(IF(od.state = 4, 1, 0)) fail_order_count, "
                    + "       SUM(IF(od.state = 3, od.total_fee, 0))           fee_count " + "from `order` od "
                    + "where date(update_at) =:dateReport and status = 1")
    Mono<GetOrderReportResponse> getOrderReport(LocalDate dateReport);

    @Query(
            value = "select count(1) order_count " + "from `order` od "
                    + "where date(create_at) =:dateReport and status = 1")
    Mono<Integer> getAllOrderReport(LocalDate dateReport);
}
