package com.ezbuy.order.repoTemplate.impl;

import com.ezbuy.order.dto.OrderDetailDTO;
import com.ezbuy.order.dto.OrderItemDTO;
import com.ezbuy.order.model.Order;
import com.ezbuy.order.repoTemplate.OrderRepositoryTemplate;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SortingUtils;
import io.r2dbc.spi.Row;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryTemplateImpl implements OrderRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<OrderDetailDTO> searchOrderDetail(
            String customerId, Integer state, int offset, int limit, String sort) {
        StringBuilder sb = new StringBuilder();
        sb.append("select *\n" +
                "from (select od1.id,\n" +
                "             od1.order_code order_code,\n" +
                "             od1.total_fee,\n" +
                "             od1.currency,\n" +
                "             od1.state,\n" +
                "             od1.create_at,\n" +
                "             oi1.id         itemId,\n" +
                "             oi1.order_id,\n" +
                "             oi1.name,\n" +
                "             oi1.description,\n" +
                "             oi1.price,\n" +
                "             oi1.origin_price,\n" +
                "             oi1.currency   oi_currency,\n" +
                "             oi1.quantity,\n" +
                "             oi1.duration\n" +
                "      from order od1\n" +
                "               left join order_item oi1\n" +
                "                         on od1.id = oi1.order_id\n" +
                "      where od1.order_code is not null\n" +
                "        and customer_id = :customerId) orderAll\n");
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
        return genericExecuteSpec.map((x, y) -> this.build(x)).all();
    }

    @Override
    public Flux<OrderDetailDTO> findOneDetailById(String customerId, String orderId) {
        return template.getDatabaseClient()
                .sql("select o.id,\n" +
                        "       o.order_code,\n" +
                        "       o.create_at,\n" +
                        "       o.total_fee,\n" +
                        "       o.currency,\n" +
                        "       o.state,\n" +
                        "       oi.id       itemId,\n" +
                        "       oi.order_id,\n" +
                        "       oi.name,\n" +
                        "       oi.description,\n" +
                        "       oi.quantity,\n" +
                        "       oi.price,\n" +
                        "from order o\n" +
                        "         join order_item oi on oi.order_id = o.id\n" +
                        "where o.id = :orderId\n" +
                        "  and o.customer_id = :customerId\n" +
                        "  and o.status = 1\n" +
                        "  and oi.status = 1")
                .bind("orderId", orderId)
                .bind("customerId", customerId)
                .map((x, y) -> this.buildDetail(x))
                .all();
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
}
