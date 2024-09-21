package com.ezbuy.authservice.repository.impl;

import static com.ezbuy.authmodel.constants.AuthConstants.Field.*;

import com.ezbuy.authmodel.dto.response.PermissionPolicyDetailDto;
import com.ezbuy.authmodel.model.PermissionPolicy;
import com.ezbuy.authservice.repository.AuthServiceCustom;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.SortingUtils;
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
        sb.append(" from sme_user.permission_policy pp where 1 = 1 ");
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

    // @Override
    // public Mono<OrganizationUnitDetailDto> getOrganizationUnitParent(String
    // organizationUnitId, String
    // organizationId, Integer state) {
    // String query = " With data_tree as (" +
    // " Select * from sme_user.organization_unit where organization_id =
    // :organizationId and id =
    // :organizationUnitId and parent_id is null and state = :state " +
    // " ) " +
    // " Select dt.*, ut.name as 'unit_name' " +
    // " from data_tree dt " +
    // " join sme_user.unit_type ut on ut.id = dt.unit_type_id ";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(STATE, state)
    // .bind(ORGANIZATION_UNIT_ID, organizationUnitId)
    // .bind(ORGANIZATION_ID, organizationId)
    // .map(this::buildOrganizationUnit)
    // .one();
    // }
    //
    // @Override
    // public Mono<OrganizationUnitDetailDto> getOrganizationUnitDetail(String
    // organizationUnitId, String
    // organizationId) {
    // String query = "With data_tree as (\n" +
    // " Select * from sme_user.organization_unit where organization_id =
    // :organizationId and id =
    // :organizationUnitId and state in(1,3) \n" +
    // " ) \n" +
    // " Select dt.*, ut.name as 'unit_name' " +
    // " from data_tree dt \n" +
    // " join sme_user.unit_type ut on ut.id = dt.unit_type_id ";
    //
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(ORGANIZATION_UNIT_ID, organizationUnitId)
    // .bind(ORGANIZATION_ID, organizationId)
    // .map(this::buildOrganizationUnit)
    // .one();
    // }
    //
    // @Override
    // public Flux<OrganizationUnitDetailDto> getOrganizationUnitDetailChild(String
    // parentId, String
    // organizationId,Integer state) {
    // String query = "Select ou.*, ut.name as 'unit_name' from
    // sme_user.organization_unit ou " +
    // " join sme_user.unit_type ut on ut.id = ou.unit_type_id " +
    // " where ou.parent_id= :parentId and ou.state=:state and ou.organization_id =
    // :organizationId " +
    // " order by create_at desc";
    //
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind("parentId", parentId)
    // .bind(STATE,state)
    // .bind(ORGANIZATION_ID, organizationId)
    // .map(this::buildOrganizationUnit)
    // .all();
    // }
    //
    // @Override
    // public Flux<OrganizationUnitDetailDto>
    // getOrganizationUnitDetailChildAll(String parentId, String
    // organizationId) {
    // String query = "Select ou.*, ut.name as 'unit_name' from
    // sme_user.organization_unit ou " +
    // " join sme_user.unit_type ut on ut.id = ou.unit_type_id " +
    // " where ou.parent_id= :parentId and ou.state in(1,3) and ou.organization_id =
    // :organizationId " +
    // " order by create_at desc";
    //
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind("parentId", parentId)
    // .bind(ORGANIZATION_ID, organizationId)
    // .map(this::buildOrganizationUnit)
    // .all();
    // }
    //
    //
    // @Override
    // public Mono<OrganizationUnitDetailDto>
    // getOrganizationUnitDetailChildById(String id, String organizationId) {
    // String query = "Select ou.*, ut.name as 'unit_name' from
    // sme_user.organization_unit ou " +
    // " join sme_user.unit_type ut on ut.id = ou.unit_type_id " +
    // " where ou.id = :id and ou.state in(1,3) and ou.organization_id =
    // :organizationId" +
    // " order by create_at desc";
    //
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind("id", id)
    // .bind(ORGANIZATION_ID, organizationId)
    // .map(this::buildOrganizationUnit)
    // .one();
    // }
    //
    // private OrganizationUnitDetailDto buildOrganizationUnit(Row obj) {
    // return OrganizationUnitDetailDto.builder()
    // .id(DataUtil.safeToString(obj.get("id")))
    // .name(DataUtil.safeToString(obj.get("name")))
    // .parentId(DataUtil.safeToString(obj.get(PARENT_ID)))
    // .code(DataUtil.safeToString(obj.get("code")))
    // .unitTypeId(DataUtil.safeToString(obj.get("unit_type_id")))
    // .address(DataUtil.safeToString(obj.get("address")))
    // .shortName(DataUtil.safeToString(obj.get("short_name")))
    // .description(DataUtil.safeToString(obj.get("description")))
    // .state(DataUtil.safeToString(obj.get(STATE)))
    // .unitTypeName(DataUtil.safeToString(obj.get("unit_name")))
    // .organizationId(DataUtil.safeToString(obj.get("organization_id")))
    // .fieldOfActivities(DataUtil.safeToString(obj.get("field_of_activity")))
    // .presentativeId(DataUtil.safeToString(obj.get("presentative_id")))
    // .placeName(DataUtil.safeToString(obj.get("place_name")))
    // .tax(DataUtil.safeToString(obj.get("tax")))
    // .build();
    // }
    //
    // @Override
    // public Flux<OrganizationUnitDto>
    // getOrganizationUnitMasterAndChild(List<String> organizationUnitId) {
    // String query = "with recursive n as( " +
    // " select id, name, code, parent_id, create_at from sme_user.organization_unit
    // where id in
    // (:organizationUnitId) and state in (1,3) " +
    // ") " +
    // "select * from n order by create_at desc ";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(ORGANIZATION_UNIT_ID, organizationUnitId)
    // .map(obj -> OrganizationUnitDto.builder()
    // .id(DataUtil.safeToString(obj.get("id")))
    // .name(DataUtil.safeToString(obj.get("name")))
    // .code(DataUtil.safeToString(obj.get("code")))
    // .parentId(DataUtil.safeToString(obj.get(PARENT_ID)))
    // .build())
    // .all();
    // }
    //
    // @Override
    // public Flux<OrganizationUnitDto> getOrganizationUnitAndChild(List<String>
    // organizationUnitId, Integer state) {
    // String query = "with recursive n as( " +
    // " select id, name, code, parent_id, create_at from sme_user.organization_unit
    // where id in
    // (:organizationUnitId) and state = :state " +
    // " union all " +
    // " select ou.id, ou.name, ou.code, ou.parent_id, ou.create_at from n join
    // sme_user.organization_unit ou on ou.parent_id = n.id where ou.state = :state
    // " +
    // ") " +
    // "select * from n order by create_at desc ";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(ORGANIZATION_UNIT_ID, organizationUnitId)
    // .bind(STATE, state)
    // .map(obj -> OrganizationUnitDto.builder()
    // .id(DataUtil.safeToString(obj.get("id")))
    // .name(DataUtil.safeToString(obj.get("name")))
    // .code(DataUtil.safeToString(obj.get("code")))
    // .parentId(DataUtil.safeToString(obj.get(PARENT_ID)))
    // .build())
    // .all();
    // }
    //
    // @Override
    // public Flux<OrganizationUnitDto> getOrganizationUnit(List<String>
    // organizationUnitId) {
    // String query = "select id, name, code, parent_id, create_at from
    // sme_user.organization_unit where id in
    // (:organizationUnitId) and state in(1,3) order by create_at desc ";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(ORGANIZATION_UNIT_ID, organizationUnitId)
    // .map(obj -> OrganizationUnitDto.builder()
    // .id(DataUtil.safeToString(obj.get("id")))
    // .name(DataUtil.safeToString(obj.get("name")))
    // .code(DataUtil.safeToString(obj.get("code")))
    // .parentId(DataUtil.safeToString(obj.get(PARENT_ID)))
    // .build())
    // .all();
    // }
    //
    // @Override
    // public Flux<OrganizationUnitDto> getOrganizationUnitChildData(String
    // organizationUnitId, Integer state) {
    // String query = "with recursive n as( " +
    // " select id, name, code, parent_id, create_at from sme_user.organization_unit
    // where parent_id =
    // :organizationUnitId and state in (1,3) " +
    // ") " +
    // "select * from n order by create_at desc ";
    // log.info("getOrganizationUnitChildData: {}", organizationUnitId);
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(ORGANIZATION_UNIT_ID, organizationUnitId)
    // .map(obj -> OrganizationUnitDto.builder()
    // .id(DataUtil.safeToString(obj.get("id")))
    // .name(DataUtil.safeToString(obj.get("name")))
    // .code(DataUtil.safeToString(obj.get("code")))
    // .parentId(DataUtil.safeToString(obj.get(PARENT_ID)))
    // .build())
    // .all();
    // }
    //
    // private IndividualUnitPositionDto buildIndividualUnitPositionDto(Row obj) {
    // return IndividualUnitPositionDto.builder()
    // .id(DataUtil.safeToString(obj.get("id")))
    // .state(DataUtil.safeToInt(obj.get(STATE)))
    // .organizationId(DataUtil.safeToString(obj.get("organization_id")))
    // .individualId(DataUtil.safeToString(obj.get("individual_id")))
    // .organizationUnitId(DataUtil.safeToString(obj.get("organization_unit_id")))
    // .positionId(DataUtil.safeToString(obj.get("position_id")))
    // .status(DataUtil.safeToInt(obj.get("status")))
    // .createAt(DataUtil.convertStringToLocalDateTime(DataUtil.safeToString(obj.get("create_at")),
    // Constants.DateTimePattern.LOCAL_DATE_TIME_PATTERN))
    // .updateAt(DataUtil.convertStringToLocalDateTime(DataUtil.safeToString(obj.get("update_at")),
    // Constants.DateTimePattern.LOCAL_DATE_TIME_PATTERN))
    // .createBy(DataUtil.safeToString(obj.get("create_by")))
    // .updateBy(DataUtil.safeToString(obj.get("update_by")))
    // .build();
    // }
    //
    // @Override
    // public Flux<IndividualUnitPositionDto>
    // getIndividualByOrganizationIdAndUserId(String organizationId, String
    // userId, Integer state) {
    // String query = "Select iup.* " +
    // "from " +
    // " sme_user.individual i " +
    // "left join sme_user.individual_unit_position iup " +
    // " on i.id = iup.individual_id " +
    // "where " +
    // " i.user_id =:userId and iup.organization_id = :organizationId and iup.state
    // =:state and
    // i.status = 1 " +
    // "group by iup.organization_unit_id ";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(ORGANIZATION_ID, organizationId)
    // .bind(STATE, state)
    // .bind("userId", userId)
    // .map(this::buildIndividualUnitPositionDto)
    // .all();
    // }
    //
    // @Override
    // public Flux<IndividualShortDto> getLeaderByEmail(String email, String
    // organizationId) {
    // String query = "select i.id, i.username, i.name, i.email from individual i
    // \n" +
    // " join individual_unit_position iup on iup.individual_id = i.id \n" +
    // " and i.email like :email and i.user_id is not null " +
    // " and iup.organization_id = :organizationId" +
    // " and i.status = 1 " +
    // " group by i.id ";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind("email", DataUtil.appendLikeQuery(email))
    // .bind(ORGANIZATION_ID, organizationId)
    // .map(row -> IndividualShortDto.builder()
    // .leaderId(DataUtil.safeToString(row.get("id")))
    // .userName(DataUtil.safeToString(row.get("username")))
    // .name(DataUtil.safeToString(row.get("name")))
    // .email(DataUtil.safeToString(row.get("email")))
    // .build())
    // .all();
    // }
    //
    // @Override
    // public Mono<IndividualShortDto> getLeaderById(String leaderId, String
    // organizationId) {
    // String query = "select i.id, i.username, i.name from individual i \n" +
    // " join individual_unit_position iup on iup.individual_id = i.id \n" +
    // " where " +
    // " i.id = :id and i.user_id is not null " +
    // " and iup.organization_id = :organizationId " +
    // " and i.status = 1 " +
    // " and iup.status = 1 " +
    // " group by i.id ";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind("id", leaderId)
    // .bind(ORGANIZATION_ID, organizationId)
    // .map(row -> IndividualShortDto.builder()
    // .leaderId(DataUtil.safeToString(row.get("id")))
    // .userName(DataUtil.safeToString(row.get("username")))
    // .name(DataUtil.safeToString(row.get("name")))
    // .build())
    // .one();
    // }
    //
    // @Override
    // public Flux<OrganizationUnitDto> getOrganizationUnitAndChild(List<String>
    // organizationUnitId) {
    // String query = " with recursive n as ( " +
    // " select id, name, code, parent_id, create_at " +
    // " from sme_user.organization_unit " +
    // " where id in (:organizationUnitId) " +
    // " and state in (1, 3) " +
    // " union all " +
    // " select u.id, u.name, u.code, u.parent_id, u.create_at " +
    // " from sme_user.organization_unit u " +
    // " inner join n " +
    // " where u.parent_id = n.id " +
    // " ) " +
    // " select distinct (id), name, code, parent_id, create_at " +
    // " from n " +
    // " order by create_at desc ";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(ORGANIZATION_UNIT_ID, organizationUnitId)
    // .map(obj -> OrganizationUnitDto.builder()
    // .id(DataUtil.safeToString(obj.get("id")))
    // .name(DataUtil.safeToString(obj.get("name")))
    // .code(DataUtil.safeToString(obj.get("code")))
    // .parentId(DataUtil.safeToString(obj.get(PARENT_ID)))
    // .build())
    // .all();
    // }
    //
    // @Override
    // public Flux<OrganizationUnitDto>
    // getOrganizationUnitTreeByRootUnitIdList(List<String> organizationUnitId) {
    // String query =
    // "with recursive n as(\n" +
    // " select id, name, code, parent_id, create_at from sme_user.organization_unit
    // where id in
    // (:organizationUnitId) and state = 1 and status = 1\n" +
    // " union all\n" +
    // " select ou.id, ou.name, ou.code, ou.parent_id, ou.create_at from
    // sme_user.organization_unit ou
    // inner join n where ou.parent_id = n.id and state = 1 and status = 1\n" +
    // ")\n" +
    // "select DISTINCT * from n order by create_at desc;";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(ORGANIZATION_UNIT_ID, organizationUnitId)
    // .map(obj -> OrganizationUnitDto.builder()
    // .id(DataUtil.safeToString(obj.get("id")))
    // .name(DataUtil.safeToString(obj.get("name")))
    // .code(DataUtil.safeToString(obj.get("code")))
    // .parentId(DataUtil.safeToString(obj.get(PARENT_ID)))
    // .build())
    // .all();
    // }
    //
    // @Override
    // public Flux<IndividualUnitPositionDto>
    // getIndividualUnitpositionsActiveByOrganizationIdAndUserId(String
    // organizationId, String userId, Integer state) {
    // String query = "Select iup.* " +
    // "from " +
    // " sme_user.individual i " +
    // "left join sme_user.individual_unit_position iup " +
    // " on i.id = iup.individual_id " +
    // "where " +
    // " i.user_id =:userId and iup.organization_id = :organizationId and iup.state
    // =:state and
    // i.status = 1 and iup.status = 1 " +
    // "group by iup.organization_unit_id ";
    // return template.getDatabaseClient()
    // .sql(query)
    // .bind(ORGANIZATION_ID, organizationId)
    // .bind(STATE, state)
    // .bind("userId", userId)
    // .map(this::buildIndividualUnitPositionDto)
    // .all();
    // }
}
