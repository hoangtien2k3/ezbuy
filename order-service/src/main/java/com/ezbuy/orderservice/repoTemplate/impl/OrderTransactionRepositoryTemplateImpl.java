package com.ezbuy.orderservice.repoTemplate.impl;

import com.ezbuy.ordermodel.dto.response.OrderTransmissionDTO;
import com.ezbuy.orderservice.repoTemplate.OrderTransactionRepositoryTemplate;
import com.reactify.util.DataUtil;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OrderTransactionRepositoryTemplateImpl extends BaseRepositoryTemplate
        implements OrderTransactionRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<OrderTransmissionDTO> searchOrderTransmission(
            String email,
            String orderCode,
            String idNo,
            String phone,
            LocalDateTime from,
            LocalDateTime to,
            int offset,
            int limit,
            String sort) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "SELECT od.id, od.order_code, od.total_fee, od.currency, od.status, od.state, od.detail_address, od.type, oi.rating \n"
                        + "oi.duration, od.id_no, od.name, od.email, od.phone, od.create_at, od.create_by, od.update_at, od.update_by \n"
                        + "FROM `order` od \n" + "INNER JOIN order_item oi ON od.id = oi.order_id \n" + "WHERE 1=1 ");
        if (!DataUtil.isNullOrEmpty(email)) {
            sb.append(" AND od.email = :email ");
        }
        if (!DataUtil.isNullOrEmpty(orderCode)) {
            sb.append(" AND od.order_code = :orderCode ");
        }
        if (!DataUtil.isNullOrEmpty(idNo)) {
            sb.append(" AND od.id_no = :idNo ");
        }
        if (!DataUtil.isNullOrEmpty(phone)) {
            sb.append(" AND od.phone = :phone ");
        }
        if (from != null) {
            sb.append(" AND od.create_at >= :from ");
        }
        if (to != null) {
            sb.append(" AND od.create_at <= :to ");
        }
        sb.append(" ").append(sort).append(" LIMIT :limit OFFSET :offset");

        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        if (!DataUtil.isNullOrEmpty(email)) {
            params.put("email", email);
        }
        if (!DataUtil.isNullOrEmpty(orderCode)) {
            params.put("orderCode", orderCode);
        }
        if (!DataUtil.isNullOrEmpty(idNo)) {
            params.put("idNo", idNo);
        }
        if (!DataUtil.isNullOrEmpty(phone)) {
            params.put("phone", phone);
        }
        if (from != null) {
            params.put("from", from);
        }
        if (to != null) {
            params.put("to", to);
        }

        return listQuery(sb.toString(), params, OrderTransmissionDTO.class);
    }

    @Override
    public Mono<Long> countOrderTransmission(
            String email, String orderCode, String idNo, String phone, LocalDateTime from, LocalDateTime to) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "SELECT od.id, od.order_code, od.total_fee, od.currency, od.status, od.state, od.detail_address, od.type,\n"
                        + "oi.rating, oi.duration, od.id_no, od.name, od.email, od.phone, od.create_at, od.create_by, od.update_at, od.update_by\n"
                        + "FROM `order` od\n" + "INNER JOIN order_item oi ON od.id = oi.order_id\n" + "WHERE 1=1 ");
        if (!DataUtil.isNullOrEmpty(email)) {
            sb.append(" and od.email = :email ");
        }
        if (!DataUtil.isNullOrEmpty(orderCode)) {
            sb.append(" and od.order_code = :orderCode ");
        }
        if (!DataUtil.isNullOrEmpty(idNo)) {
            sb.append(" and od.id_no = :idNo ");
        }
        if (!DataUtil.isNullOrEmpty(phone)) {
            sb.append(" and od.phone = :phone ");
        }
        if (from != null) {
            sb.append(" and od.create_at >= :from ");
        }
        if (to != null) {
            sb.append(" and od.create_at <= :to ");
        }

        Map<String, Object> parrams = new HashMap<>();
        if (!DataUtil.isNullOrEmpty(email)) {
            parrams.put("email", email);
        }
        if (!DataUtil.isNullOrEmpty(orderCode)) {
            parrams.put("orderCode", orderCode);
        }
        if (!DataUtil.isNullOrEmpty(idNo)) {
            parrams.put("idNo", idNo);
        }
        if (!DataUtil.isNullOrEmpty(phone)) {
            parrams.put("phone", phone);
        }
        if (from != null) {
            parrams.put("from", from);
        }
        if (to != null) {
            parrams.put("to", to);
        }

        return countQuery(sb.toString(), parrams);
    }
}
