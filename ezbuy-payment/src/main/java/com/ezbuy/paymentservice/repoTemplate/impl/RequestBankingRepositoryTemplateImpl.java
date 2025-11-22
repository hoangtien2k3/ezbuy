package com.ezbuy.paymentservice.repoTemplate.impl;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.ezbuy.paymentservice.model.dto.RequestBankingSyncDTO;
import com.ezbuy.paymentservice.model.dto.UpdateOrderStateDTO;
import com.ezbuy.paymentservice.model.dto.request.UpdateOrderStateRequest;
import com.ezbuy.paymentservice.model.entity.RequestBanking;
import com.ezbuy.paymentservice.repoTemplate.RequestBankingRepositoryTemplate;
import io.r2dbc.spi.Statement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class RequestBankingRepositoryTemplateImpl implements RequestBankingRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    private final String SYSTEM = "system";
    private final Integer COMPLETED = 1;

    @Override
    public Flux<RequestBanking> getRequestBankingByListOrderCode(UpdateOrderStateRequest request) {
        return template.select(RequestBanking.class)
                .matching(query(where("order_id")
                        .in(request.getUpdateOrderStateDTOList().stream()
                                .map(UpdateOrderStateDTO::getOrderCode)
                                .collect(Collectors.toList()))
                        .and("order_type")
                        .in(request.getUpdateOrderStateDTOList().stream()
                                .map(UpdateOrderStateDTO::getOrderType)
                                .collect(Collectors.toList()))
                        .and("order_state")
                        .is(0)
                        .and("state")
                        .is(1)
                        .and("status")
                        .is(1)
                        .and("vt_transaction_id")
                        .isNotNull()
                        .and("update_state")
                        .isNull()))
                .all();
    }

    @Override
    public Flux<Object> updateRequestBankingBatch(Map<String, Integer> request) {
        String updateQuery = """
            UPDATE request_banking
            SET order_state = ?,
                update_state = ?,
                update_at = NOW(),
                update_by = ?
            WHERE id = ? AND status = 1
        """;
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            for (String key : request.keySet()) {
                statement.bind(0, COMPLETED)
                        .bind(1, request.get(key))
                        .bind(2, SYSTEM)
                        .bind(3, key)
                        .add();
            }
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }

    @Override
    public Flux<Object> updateRequestBankingBatchForSync(List<RequestBankingSyncDTO> requestBankingSyncDTOList) {
        String updateQuery = """
            UPDATE request_banking
            SET order_state = ?,
                update_state = ?,
                update_at = NOW(),
                update_by = ?
            WHERE id = ? AND status = 1
        """;
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            requestBankingSyncDTOList.forEach(requestBankingSyncDTO -> {
                statement.bind(0, requestBankingSyncDTO.getVtTransactionId())
                        .bind(1, SYSTEM)
                        .bind(2, requestBankingSyncDTO.getPaymentStatus())
                        .bind(3, requestBankingSyncDTO.getUpdateState())
                        .bind(4, requestBankingSyncDTO.getId())
                        .add();
            });
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }
}
