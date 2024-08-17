package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.framework.utils.SortingUtils;
import com.ezbuy.settingmodel.dto.SettingDTO;
import com.ezbuy.settingmodel.model.Setting;
import com.ezbuy.settingmodel.request.SearchSettingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class SettingRepositoryTemplateImpl extends BaseRepositoryTemplate implements SettingRepositoryTemplate {
    @Override
    public Flux<SettingDTO> searchSettingByRequest(SearchSettingRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = "update_at DESC, create_at DESC \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), Setting.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQuery(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n")
                .append(" LIMIT :pageSize  \n" +
                        "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);
        return listQuery(query.toString(), params, SettingDTO.class);
    }

    @Override
    public Mono<Long> count(SearchSettingRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(builder, params, request);
        return countQuery(builder.toString(), params);
    }

    private void buildQuery(StringBuilder builder, Map<String, Object> params, SearchSettingRequest request) {

        builder.append("select * from sme_setting.setting \n");
        builder.append("where 1 = 1 \n");
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and setting.code like CONCAT('%', :code, '%')");
            params.put("code", request.getCode());
            System.out.println(builder);
        }
        if (!Objects.isNull(request.getStatus())) {
            builder.append(" and setting.status = :status ");
            params.put("status", request.getStatus());
        }
        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and setting.create_at >= :fromDate\n");
            params.put("fromDate", getFromDate(request.getFromDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and setting.create_at <= :toDate\n");
            params.put("toDate", getToDate(request.getToDate()));
        }
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate.atTime(0, 0, 0);
    }
    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate.atTime(23, 59, 59);
    }
}
