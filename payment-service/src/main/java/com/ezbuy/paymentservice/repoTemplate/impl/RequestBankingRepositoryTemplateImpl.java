package com.ezbuy.paymentservice.repoTemplate.impl;

import com.ezbuy.paymentservice.repoTemplate.RequestBankingRepositoryTemplate;
import com.viettel.sme.ordermodel.constants.Constants;
import com.ezbuy.paymentmodel.constants.OrderState;
import com.ezbuy.paymentmodel.dto.RequestBankingSyncDTO;
import com.ezbuy.paymentmodel.dto.UpdateOrderStateDTO;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateRequest;
import com.ezbuy.paymentmodel.model.RequestBanking;
import io.r2dbc.spi.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.r2dbc.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class RequestBankingRepositoryTemplateImpl implements RequestBankingRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<RequestBanking> getRequestBankingByListOrderCode(UpdateOrderStateRequest request) {
        return template.select(RequestBanking.class)
                .matching(query(where("order_id").in(request.getUpdateOrderStateDTOList().stream().map(UpdateOrderStateDTO::getOrderCode).collect(Collectors.toList()))
                        .and("order_type").in(request.getUpdateOrderStateDTOList().stream().map(UpdateOrderStateDTO::getOrderType).collect(Collectors.toList()))
                        .and("order_state").is(0)
                        .and("state").is(1)
                        .and("status").is(1)
                        .and("vt_transaction_id").isNotNull()
                        .and("update_state").isNull()))
                .all();
    }

    @Override
    public Flux<Object> updateRequestBankingBatch(Map<String, Integer> request) {
        String updateQuery = "update request_banking set order_state = ?, update_state = ?, update_at = NOW(), update_by = ?" +
                " where id = ? and status = 1";
        return template.getDatabaseClient()
                .inConnectionMany(connection -> {
                    Statement statement = connection.createStatement(updateQuery);
                    for (String key: request.keySet()) {

                        statement.bind(0, OrderState.COMPLETED.getValue())
                                .bind(1, request.get(key))
                                .bind(2, Constants.Actor.SYSTEM)
                                .bind(3, key)
                                .add();
                    }

                    return Flux.from(statement.execute())
                            .flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
                });
    }

    @Override
    public Flux<Object> updateRequestBankingBatchForSync(List<RequestBankingSyncDTO> requestBankingSyncDTOList) {
        String updateQuery = "update request_banking set vt_transaction_id = ?, update_at = NOW(), update_by = ?, state= ?, update_state = ?" +
                " where id = ? and status = 1";
        return template.getDatabaseClient()
                .inConnectionMany(connection -> {
                    Statement statement = connection.createStatement(updateQuery);
                    requestBankingSyncDTOList.forEach(requestBankingSyncDTO -> {
                        statement.bind(0, requestBankingSyncDTO.getVtTransactionId())
                                .bind(1, Constants.Actor.SYSTEM)
                                .bind(2, requestBankingSyncDTO.getPaymentStatus())
                                .bind(3, requestBankingSyncDTO.getUpdateState())
                                .bind(4, requestBankingSyncDTO.getId())
                                .add();
                    });
                    return Flux.from(statement.execute())
                            .flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
                });
    }
}
