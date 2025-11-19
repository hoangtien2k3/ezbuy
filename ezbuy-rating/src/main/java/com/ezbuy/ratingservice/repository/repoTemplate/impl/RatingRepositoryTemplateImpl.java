package com.ezbuy.ratingservice.repository.repoTemplate.impl;

import com.ezbuy.ratingmodel.dto.RatingDTO;
import com.ezbuy.ratingmodel.request.FindRatingRequest;
import com.ezbuy.ratingservice.repository.repoTemplate.RatingRepositoryTemplate;
import com.ezbuy.core.repository.BaseTemplateRepository;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SQLUtils;
import com.ezbuy.core.util.SortingUtils;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RatingRepositoryTemplateImpl extends BaseTemplateRepository implements RatingRepositoryTemplate {

    @Override
    public Flux<RatingDTO> findRating(FindRatingRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryServiceRating(query, params, request);
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " create_at \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), RatingDTO.class);
        }
        query.append(" ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);
        return listQuery(query.toString(), params, RatingDTO.class);
    }

    private void buildQueryServiceRating(StringBuilder builder, Map<String, Object> params, FindRatingRequest request) {
        builder.append("select * from rating \n" + "where 1=1");
        if (!DataUtil.isNullOrEmpty(request.getRatingTypeCode())) {
            builder.append(" and rating_type_code = :ratingTypeCode ");
            params.put("ratingTypeCode", request.getRatingTypeCode());
        }
        if (!DataUtil.isNullOrEmpty(request.getTargetId())) {
            builder.append(" and target_id = :targetId ");
            params.put("targetId", request.getTargetId());
        }
        if (!DataUtil.isNullOrEmpty(request.getUsername())) {
            builder.append(" and username = :username ");
            params.put("username", request.getUsername());
        }
        if (!DataUtil.isNullOrEmpty(request.getCustName())) {
            builder.append(" and cust_name LIKE '%' || :custName || '%' ");
            params.put("custName", request.getCustName());
        }
        if (request.getRating() != null && request.getRating() != 0) {
            builder.append(" and rating = :rating ");
            params.put("rating", SQLUtils.replaceSpecialDigit(String.valueOf(request.getRating())));
        }
        if (request.getRatingStatus() != null) {
            builder.append(" and status = :status");
            params.put("status", request.getRatingStatus());
        }
        if (!DataUtil.isNullOrEmpty(request.getState())) {
            builder.append(" and state = :state ");
            params.put("state", request.getState());
        }
        if (request.getDisplayStatus() != null) {
            builder.append(" and display_status = :displayStatus");
            params.put("displayStatus", request.getDisplayStatus());
        }
        if (request.getSumRateStatus() != null) {
            builder.append(" and sum_rate_status = :sumRateStatus");
            params.put("sumRateStatus", request.getSumRateStatus());
        }
        if (!DataUtil.isNullOrEmpty(request.getTargetUser())) {
            builder.append(" and target_user = :targetUser ");
            params.put("targetUser", request.getTargetUser());
        }
        if (request.getFromDate() != null) {
            builder.append(" and rating_date >= to_timestamp(:fromDate / 1000) ");
            params.put("fromDate", request.getFromDate());
        }
        if (request.getToDate() != null) {
            builder.append(" and rating_date <= to_timestamp(:toDate / 1000)  ");
            params.put("toDate", request.getToDate());
        }
    }

    @Override
    public Mono<Long> countServiceRating(FindRatingRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryServiceRating(builder, params, request);
        return countQuery(builder.toString(), params);
    }
}
