package com.ezbuy.authservice.repository.impl;

import com.ezbuy.authmodel.dto.UserProfileDTO;
import com.ezbuy.authmodel.dto.request.QueryUserRequest;
import com.ezbuy.authmodel.model.UserProfile;
import com.ezbuy.authservice.repository.UserRepositoryTemplate;
import io.hoangtien2k3.commons.repository.BaseTemplateRepository;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.SQLUtils;
import io.hoangtien2k3.commons.utils.SortingUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryTemplateImpl extends BaseTemplateRepository implements UserRepositoryTemplate {

    @Override
    public Flux<UserProfileDTO> queryUserProfile(QueryUserRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        buildQueryUserProfile(builder, params, request);

        Integer pageIndex = request.getPageIndex();
        Integer pageSize = request.getPageSize();

        if (pageIndex != null && pageSize != null) {
            builder.append("limit ")
                    .append((pageIndex - 1) * pageSize)
                    .append(", ")
                    .append(pageSize);
        }

        return listQuery(builder.toString(), params, UserProfileDTO.class);
    }

    @Override
    public Mono<Long> countUserProfile(QueryUserRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        buildQueryUserProfile(builder, params, request);

        return countQuery(builder.toString(), params);
    }

    private void buildQueryUserProfile(StringBuilder builder, Map<String, Object> params, QueryUserRequest request) {
        builder.append("select * from user_profile \n");
        builder.append("where 1 = 1 \n");

        String sort = DataUtil.safeToString(request.getSort(), "-createAt");

        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and create_at >= :from \n");
            params.put("from", request.getFromDate().atStartOfDay());
        }

        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and create_at <= :to \n");
            params.put("to", request.getToDate().plusDays(1).atStartOfDay());
        }

        if (!DataUtil.isNullOrEmpty(request.getCompanyName())) {
            builder.append("and company_name like concat('%', :companyName, '%') \n");
            params.put(
                    "companyName",
                    SQLUtils.replaceSpecialDigit(request.getCompanyName().trim()));
        }

        if (!DataUtil.isNullOrEmpty(request.getPhoneNumber())) {
            builder.append("and phone like concat('%',:phone, '%') \n");
            params.put("phone", request.getPhoneNumber().trim());
        }

        if (!DataUtil.isNullOrEmpty(request.getTaxCode())) {
            builder.append("and tax_code like concat('%', :taxCode, '%') \n");
            params.put(
                    "taxCode", SQLUtils.replaceSpecialDigit(request.getTaxCode().trim()));
        }

        if (!DataUtil.isNullOrEmpty(request.getProvinceCode())) {
            builder.append("and province_code like :provinceCode \n");
            params.put("provinceCode", request.getProvinceCode().trim());
        }

        if (!DataUtil.isNullOrEmpty(request.getUserIds())) {
            builder.append("and user_id in (:kcUserIds) \n");
            params.put("kcUserIds", request.getUserIds());
        }

        if (!DataUtil.isNullOrEmpty(request.getIds())) {
            builder.append("and id in (:ids) \n");
            params.put("ids", request.getIds());
        }

        String sortValue = SortingUtils.parseSorting(sort, UserProfile.class);
        builder.append(" order by ");
        if (!DataUtil.isNullOrEmpty(sortValue)) {
            builder.append(sortValue).append(" \n");
        } else {
            builder.append(" create_at DESC \n");
        }
    }
}
