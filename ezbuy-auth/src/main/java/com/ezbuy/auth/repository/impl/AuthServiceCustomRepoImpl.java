package com.ezbuy.auth.repository.impl;

import static com.ezbuy.authmodel.constants.AuthConstants.Field.*;

import com.ezbuy.authmodel.dto.response.PermissionPolicyDetailDto;
import com.ezbuy.authmodel.model.PermissionPolicy;
import com.ezbuy.auth.repository.AuthServiceCustom;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SortingUtils;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AuthServiceCustomRepoImpl implements AuthServiceCustom {
    private R2dbcEntityTemplate template;

    @Override
    public Mono<Integer> countPermissionPolicyDetail(String filter, Integer state, String sort) {
        DatabaseClient.GenericExecuteSpec genericExecuteSpec =
                buildSqlGetPermisstion(filter, state, null, null, sort, true);
        return genericExecuteSpec
                .map(x -> DataUtil.safeToInt(x.get("total"), 0))
                .one();
    }

    @Override
    public Flux<PermissionPolicyDetailDto> getPermissionPolicyDetail(
            String filter, Integer state, Integer offset, Integer pageSize, String sort) {
        DatabaseClient.GenericExecuteSpec genericExecuteSpec =
                buildSqlGetPermisstion(filter, state, offset, pageSize, sort, false);
        return genericExecuteSpec
                .map(x -> buidPermissionPolicyDetailDto((Row) x))
                .all();
    }

    private DatabaseClient.GenericExecuteSpec buildSqlGetPermisstion(
            String filter, Integer state, Integer offset, Integer pageSize, String sort, boolean isCount) {
        StringBuilder sb = new StringBuilder();
        if (isCount) {
            sb.append(" Select count(*) as total ");
        } else {
            sb.append("Select * ");
        }
        sb.append(" from permission_policy pp where 1 = 1 ");
        if (state != null) {
            sb.append(" and pp.status = :state");
        }
        if (filter != null) {
            sb.append(" and pp.description = :filter");
        }
        String sortValue = SortingUtils.parseSorting(sort, PermissionPolicy.class);
        sb.append(" order by ");
        if (!DataUtil.isNullOrEmpty(sortValue)) {
            sb.append(sortValue);
        } else {
            sb.append(" create_at DESC");
        }
        if (!isCount) {
            sb.append(" limit :limit offset :offset ");
        }
        DatabaseClient.GenericExecuteSpec genericExecuteSpec =
                template.getDatabaseClient().sql(sb.toString());
        if (!isCount) {
            genericExecuteSpec = genericExecuteSpec.bind("offset", offset).bind("limit", pageSize);
        }

        if (state != null) {
            genericExecuteSpec = genericExecuteSpec.bind(STATE, state);
        }
        if (filter != null) {
            genericExecuteSpec = genericExecuteSpec.bind("filter", "%" + filter + "%");
        }
        return genericExecuteSpec;
    }

    private PermissionPolicyDetailDto buidPermissionPolicyDetailDto(Row row) {
        return PermissionPolicyDetailDto.builder()
                .id(DataUtil.safeToString(row.get("id"), ""))
                .name(DataUtil.safeToString(row.get("value"), ""))
                .type(DataUtil.safeToString(row.get("type"), ""))
                .code(DataUtil.safeToString(row.get("code"), ""))
                .state(DataUtil.safeToInt(row.get("status")))
                .description(DataUtil.safeToString(row.get("description"), ""))
                .individualOrganizationPermissionsId(
                        DataUtil.safeToString(row.get("individual_organization_permissions_id"), ""))
                .build();
    }
}
