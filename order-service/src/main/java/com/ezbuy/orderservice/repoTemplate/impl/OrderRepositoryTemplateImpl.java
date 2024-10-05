package com.ezbuy.orderservice.repoTemplate.impl;

import com.ezbuy.sme.framework.utils.DataUtil;
import com.ezbuy.sme.framework.utils.SortingUtils;
import com.ezbuy.sme.ordermodel.constants.Constants;
import com.ezbuy.sme.ordermodel.dto.OrderDetailDTO;
import com.ezbuy.sme.ordermodel.dto.OrderItemDTO;
import com.ezbuy.sme.ordermodel.dto.OrderSyncDTO;
import com.ezbuy.sme.ordermodel.model.Characteristic;
import com.ezbuy.sme.ordermodel.model.Order;
import com.ezbuy.sme.ordermodel.model.response.CustomerSubscriberSmeInfoDTO;
import com.ezbuy.orderservice.repoTemplate.OrderRepositoryTemplate;
import com.ezbuy.sme.paymentmodel.constants.OrderState;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryTemplateImpl implements OrderRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<OrderDetailDTO> searchOrderDetail(String customerId, Integer state, int offset, int limit, String sort) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from (select od1.id, od1.order_code order_code, od1.total_fee, od1.currency, od1.state, od1.create_at, " +
                "       oi1.id itemId, oi1.order_id, oi1.name, oi1.description, oi1.price, oi1.origin_price, oi1.currency oi_currency, " +
                "       oi1.quantity, oi1.duration " +
                "from `order` od1 " +
                "         left join order_item oi1 " +
                "                   on od1.id = oi1.order_id " +
                "where od1.order_code is not null " +
                "  and customer_id = :customerId " +
                "union " +
                "select oi2.id, oi2.order_code order_code, oi2.price total_fee, oi2.currency, oi2.state, oi2.create_at, " +
                "       oi2.id itemId, oi2.order_id, oi2.name, oi2.description, oi2.price, oi2.origin_price, oi2.currency oi_currency, " +
                "       oi2.quantity, if(oi2.duration is null, (select value from characteristic where sme_order.characteristic.order_item_id = oi2.id and name like '%DURATION%'), oi2.duration) duration " +
                "from order_item oi2 " +
                "         left join `order` od2 " +
                "                   on oi2.order_id = od2.id " +
                "where oi2.order_code is not null " +
                "  and customer_id = :customerId) orderAll");

        if (state != null) {
            sb.append(" where state = :state");
        }

        String sortValue = SortingUtils.parseSorting(sort, Order.class);
        sb.append(" order by ");
        if (!DataUtil.isNullOrEmpty(sortValue)) {
            sb.append(sortValue);
        } else {
            sb.append(" create_at DESC");
        }
        sb.append(" limit :limit offset :offset");
        DatabaseClient.GenericExecuteSpec genericExecuteSpec = template.getDatabaseClient()
                .sql(sb.toString())
                .bind("customerId", customerId)
                .bind("offset", offset)
                .bind("limit", limit);

        if (state != null) {
            genericExecuteSpec = genericExecuteSpec.bind("state", state);
        }

        return genericExecuteSpec
                .map(this::build)
                .all();
    }

    @Override
    public Flux<OrderDetailDTO> findOneDetailById(String customerId, String orderId) {
        return template.getDatabaseClient()
                .sql("select o.id, o.order_code, o.create_at, o.total_fee, o.currency, o.state, " +
                        " oi.id itemId, oi.order_id, oi.name, oi.description, oi.currency oi_currency , oi.quantity, oi.price," +
                        " oi.duration, oi.origin_price, oi.telecom_service_id, oi.telecom_service_name, oi.telecom_service_alias " +
                        " from `order` o" +
                        " join order_item oi on oi.order_id = o.id " +
                        " where o.id = :orderId" +
                        " and o.customer_id = :customerId" +
                        " and o.status = 1 and oi.status = 1")
                .bind("orderId", orderId)
                .bind("customerId", customerId)
                .map(this::buildDetail)
                .all();
    }

    @Override
    public Flux<Object> updateOrderBatch(List<OrderSyncDTO> orderList) {
        String updateQuery = "update `order` set state = ?, update_at = NOW(), update_by = ?, description = ?" +
                " where id = ? and status = 1";
        return getObjectFlux(orderList, updateQuery);
    }

    @Override
    public Flux<Object> updateOrderItemBatch(List<OrderSyncDTO> orderList) {
        String updateQuery = "update order_item set state = ?, update_at = NOW(), update_by = ?, description = ?" +
                " where id = ? and status = 1";
        return getObjectFlux(orderList, updateQuery);
    }

    private Flux<Object> getObjectFlux(List<OrderSyncDTO> orderList, String updateQuery) {
        if (DataUtil.isNullOrEmpty(orderList)) {
            return Flux.just(new Object());
        }
        return template.getDatabaseClient()
                .inConnectionMany(connection -> {
                    Statement statement = connection.createStatement(updateQuery);
                    orderList.forEach(order ->
                            statement.bind(0, order.getState())
                                    .bind(1, Constants.Actor.SYSTEM)
                                    .bind(2, order.getDescription())
                                    .bind(3, order.getId())
                                    .add()
                    );
                    return Flux.from(statement.execute())
                            .flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
                });
    }

    @Override
    public Flux<CustomerSubscriberSmeInfoDTO> findCustomerSubscriberSmeInfoById(String id) {
        // bo sung tra ve alias oi.telecom_service_alias
        return template.getDatabaseClient()
                .sql("SELECT o.state as order_state,oi.is_bundle, oi.order_id, o.id_no, oi.id, oi.subscriber_id, oi.telecom_service_id, oi.telecom_service_alias, " +
                        "oi.product_id, oi.product_code, oi.name, oi.price, c.id as c_id, c.name as c_name, " +
                        "c.value_type as c_value_type, c.value as c_value, c.code as c_code, o.individual_id as individual_id " +
                        "FROM order_item oi " +
                        "INNER JOIN `order` o ON oi.order_id = o.id " +
                        "LEFT JOIN `characteristic` c ON oi.id = c.order_item_id " +
                        "WHERE o.id = :orderId and o.status = 1 and oi.status = 1")
                .bind("orderId", id)
                .map((row, metadata) -> {

                    CustomerSubscriberSmeInfoDTO customerSubscriberSmeInfoDTO = new CustomerSubscriberSmeInfoDTO();

                    customerSubscriberSmeInfoDTO.setIdNo(row.get("id_no", String.class));
                    customerSubscriberSmeInfoDTO.setIsdn(row.get("subscriber_id", String.class));
                    customerSubscriberSmeInfoDTO.setTelecomServiceId(row.get("telecom_service_id", String.class));
                    customerSubscriberSmeInfoDTO.setTelecomServiceAlias(row.get("telecom_service_alias", String.class)); // bo sung alias theo PYCXXX/LuongToanTrinhScontract
                    customerSubscriberSmeInfoDTO.setProductId(row.get("product_id", String.class));
                    customerSubscriberSmeInfoDTO.setProductCode(row.get("product_code", String.class));
                    customerSubscriberSmeInfoDTO.setName(row.get("name", String.class));
                    customerSubscriberSmeInfoDTO.setSubscriberId(row.get("subscriber_id", String.class));
                    customerSubscriberSmeInfoDTO.setPrice(row.get("price", Double.class));
                    customerSubscriberSmeInfoDTO.setId(row.get("id", String.class));
                    customerSubscriberSmeInfoDTO.setOrderState(row.get("order_state", Integer.class));
                    customerSubscriberSmeInfoDTO.setIsBundle(row.get("is_bundle", Integer.class));
                    customerSubscriberSmeInfoDTO.setIndividualId(row.get("individual_id", String.class));

                    // Ánh xạ danh sách các đặc tính từ kết quả truy vấn
                    Characteristic characteristic = new Characteristic();
                    characteristic.setId(row.get("c_id", String.class));
                    characteristic.setName(row.get("c_name", String.class));
                    characteristic.setValueType(row.get("c_value_type", String.class));
                    characteristic.setValue(row.get("c_value", String.class));
                    characteristic.setCode(row.get("c_code", String.class));

                    customerSubscriberSmeInfoDTO.setCharacteristic(characteristic);
                    return customerSubscriberSmeInfoDTO;
                }).all();
    }

    @Override
    public Flux<String> updateOrderItemsState(Map<String, String> orderIds) {
        String updateQuery = "update `order_item` set order_code = ?, state = ?, update_at = NOW(), update_by = ?" +
                " where id = ? and status = 1";
        return template.getDatabaseClient()
                .inConnectionMany(connection -> {
                    Statement statement = connection.createStatement(updateQuery);
                    for (Map.Entry<String, String> entry : orderIds.entrySet()) {
                        statement.bind(0, entry.getValue())
                                .bind(1, OrderState.IN_PROGRESS.getValue())
                                .bind(2, Constants.Actor.SYSTEM)
                                .bind(3, entry.getKey())
                                .add();
                    }
                    return Flux.from(statement.execute())
                            .flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
                });
    }

    private OrderDetailDTO build(Row row) {
        OrderItemDTO orderItem = OrderItemDTO.builder()
                .id(DataUtil.safeToString(row.get("itemId")))
                .orderId(DataUtil.safeToString(row.get("id")))
                .name(DataUtil.safeToString(row.get("name")))
                .description(DataUtil.safeToString(row.get("description")))
                .quantity(DataUtil.safeToInt(row.get("quantity")))
                .currency(DataUtil.safeToString(row.get("oi_currency")))
                .price(DataUtil.safeToLong(row.get("price")))
                .originPrice(DataUtil.safeToLong(row.get("origin_price"), null))
                .duration(DataUtil.safeToString(row.get("duration"), null))
                .build();

        List<OrderItemDTO> items = new ArrayList<>();
        items.add(orderItem);

        return OrderDetailDTO.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .orderCode((DataUtil.safeToString(row.get("order_code"))))
                .currency((DataUtil.safeToString(row.get("currency"))))
                .createAt(row.get("create_at"))
                .totalFee(DataUtil.safeToLong(row.get("total_fee")))
                .state(DataUtil.safeToInt(row.get("state")))
                .itemList(items)
                .build();
    }

    private OrderDetailDTO buildDetail(Row row) {
        OrderItemDTO orderItem = OrderItemDTO.builder()
                .id(DataUtil.safeToString(row.get("itemId")))
                .orderId(DataUtil.safeToString(row.get("id")))
                .name(DataUtil.safeToString(row.get("name")))
                .description(DataUtil.safeToString(row.get("description")))
                .quantity(DataUtil.safeToInt(row.get("quantity")))
                .currency(DataUtil.safeToString(row.get("oi_currency")))
                .price(DataUtil.safeToLong(row.get("price")))
                .telecomServiceId(DataUtil.safeToString(row.get("telecom_service_id")))
                .telecomServiceName(DataUtil.safeToString(row.get("telecom_service_name")))
                .telecomServiceAlias(DataUtil.safeToString(row.get("telecom_service_alias")))
                .originPrice(DataUtil.safeToLong(row.get("origin_price"), null))
                .duration(DataUtil.safeToString(row.get("duration"), null))
                .build();

        List<OrderItemDTO> items = new ArrayList<>();
        items.add(orderItem);

        return OrderDetailDTO.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .orderCode((DataUtil.safeToString(row.get("order_code"))))
                .createAt(row.get("create_at"))
                .state(DataUtil.safeToInt(row.get("state")))
                .currency(DataUtil.safeToString(row.get("currency")))
                .totalFee(DataUtil.safeToLong(row.get("total_fee")))
                .itemList(items)
                .build();
    }

    @Override
    public Flux<OrderDetailDTO> searchOrderDetailV2(String customerId, Integer state, int offset, int limit, String sort, List<String> orderCodeList) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from (select od1.id, od1.order_code order_code, od1.total_fee, od1.currency, od1.state, od1.create_at, " +
                "       oi1.id itemId, oi1.order_id, oi1.name, oi1.description, oi1.price, oi1.origin_price, oi1.currency oi_currency, " +
                "       oi1.quantity, oi1.duration " +
                "from `order` od1 " +
                "         left join order_item oi1 " +
                "                   on od1.id = oi1.order_id " +
                "where customer_id = :customerId " +
                "union " +
                "select oi2.id, oi2.order_code order_code, oi2.price total_fee, oi2.currency, oi2.state, oi2.create_at, " +
                "       oi2.id itemId, oi2.order_id, oi2.name, oi2.description, oi2.price, oi2.origin_price, oi2.currency oi_currency, " +
                "       oi2.quantity, if(oi2.duration is null, (select value from characteristic where sme_order.characteristic.order_item_id = oi2.id and name like '%DURATION%'), oi2.duration) duration " +
                "from order_item oi2 " +
                "         left join `order` od2 " +
                "                   on oi2.order_id = od2.id " +
                "where customer_id = :customerId) orderAll where 1 = 1");
        if (state != null) {
            sb.append(" and state = :state");
        }
        sb.append(" and (order_code = null");
        if (!DataUtil.isNullOrEmpty(orderCodeList)) {
            sb.append(" or order_code in (:orderCodeList))");
        } else {
            sb.append(")");
        }
        String sortValue = SortingUtils.parseSorting(sort, Order.class);
        sb.append(" order by ");
        if (DataUtil.isNullOrEmpty(sortValue)) {
            sb.append(" create_at DESC");
        } else {
            sb.append(sortValue);
        }
        sb.append(" limit :limit offset :offset");
        DatabaseClient.GenericExecuteSpec genericExecuteSpec = template.getDatabaseClient()
                .sql(sb.toString())
                .bind("customerId", customerId)
                .bind("offset", offset)
                .bind("limit", limit);
        if (state != null) {
            genericExecuteSpec = genericExecuteSpec.bind("state", state);
        }
        if (!DataUtil.isNullOrEmpty(orderCodeList)) {
            genericExecuteSpec = genericExecuteSpec.bind("orderCodeList", orderCodeList);
        }
        return genericExecuteSpec
                .map(this::build)
                .all();
    }
}
