package com.ezbuy.orderservice.repoTemplate.impl;

import com.ezbuy.orderservice.repoTemplate.InvoiceInfoHistoryRepositoryTemplate;
import com.ezbuy.sme.framework.utils.DataUtil;
import com.ezbuy.sme.framework.utils.SortingUtils;
import com.ezbuy.sme.ordermodel.model.InvoiceInfoHistory;
import com.ezbuy.sme.ordermodel.dto.request.GetInvoiceInfoHistoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class InvoiceInfoHistoryRepositoryTemplateImpl extends BaseRepositoryTemplate implements InvoiceInfoHistoryRepositoryTemplate {
    private void buildQuery(StringBuilder builder, Map<String, Object> params, GetInvoiceInfoHistoryRequest request) {
        builder.append("select * from invoice_info_history \n");
        builder.append("where 1 = 1 \n");
        if (!Objects.isNull(request.getUserId())) {
            builder.append(" and user_id = :userId ");
            params.put("userId", request.getUserId());
        }
        if (!Objects.isNull(request.getOrganizationId())) {
            builder.append(" and  organization_id = :organization_id ");
            params.put("organization_id", request.getOrganizationId());
        }
        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and create_at >= :fromDate\n");
            params.put("fromDate", getFromDate(request.getFromDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and create_at <= :toDate\n");
            params.put("toDate", getToDate(request.getToDate()));
        }
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate.atTime(0, 0, 0);
    }
    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate.atTime(23, 59, 59);
    }

    @Override
    public Flux<InvoiceInfoHistory> findInvoiceInfoHistory(GetInvoiceInfoHistoryRequest request) {
        String sorting = "create_at DESC \n";
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQuery(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n")
                .append(" LIMIT :pageSize");
        params.put("pageSize", 5);
        return listQuery(query.toString(), params, InvoiceInfoHistory.class);
    }

}
