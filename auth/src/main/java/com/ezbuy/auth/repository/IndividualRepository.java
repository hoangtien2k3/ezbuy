package com.ezbuy.auth.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ezbuy.auth.model.dto.response.IndividualDTO;
import com.ezbuy.auth.model.postgresql.Individual;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IndividualRepository extends R2dbcRepository<Individual, UUID> {

    @Query("select * from individual where lower(code) = lower(:code) and status =:status")
    Mono<Individual> findByCode(String code, Integer status);

    @Query(
            value = "select *\n" + "from individual\n"
                    + "         inner join individual_unit_position iup on individual.id = iup.individual_id\n"
                    + "         inner join positions p on iup.position_id = p.id\n"
                    + "where iup.organization_id = :organizationId\n"
                    + "  and iup.status = :unitStatus\n"
                    + "  and p.status = :positionStatus\n"
                    + "  and p.is_system = :isSystem\n"
                    + "  and p.code = :code\n"
                    + "  and iup.organization_unit_id = (select id\n"
                    + "                                  from organization_unit ou\n"
                    + "                                  where ou.parent_id is null\n"
                    + "                                    and ou.organization_id = :organizationId limit 1)\n"
                    + "limit 1")
    Mono<Individual> findRepresentative(
            String organizationId, int unitStatus, int positionStatus, int isSystem, String code);

    @Query(
            value = "select individual.id\n"
                    + "from individual left join individual_unit_position iup on individual.id = iup.individual_id\n"
                    + "where individual_id is null and individual.user_id=:userId limit 1")
    Mono<String> findIndividualIdByUserIdAndNotInPosition(String userId);

    @Query(
            value = "select iup.individual_id from individual_unit_position iup \n"
                    + "join individual i on i.id = iup.individual_id\n"
                    + "where i.user_id=:userId and iup.organization_id=:organizationId\n"
                    + "limit 1")
    Mono<String> findIndividualIdByUserIdAndOrganizationId(String userId, String organizationId);

    @Query(
            value = "select i.* from individual_unit_position iup \n"
                    + "join individual i on i.id = iup.individual_id\n"
                    + "where i.code=:code and iup.organization_id=:organizationId and i.status in (0,1) and iup.status = 1\n"
                    + "limit 1")
    Mono<Individual> findIndividualIdByCodeAndOrganizationId(String code, String organizationId);

    @Query(value = "select * from individual where status in (0,1) and id =:id")
    Mono<Individual> findById(String id);

    @Query("select * from individual where id =:id and status =:status")
    Mono<Individual> findByIdAndState(String id, Integer status);

    @Query(value = "select * from individual\n" + "where id=:id and status=:status")
    Mono<Individual> getIndividualByIdAnAndStatus(String id, Integer status);

    @Query(value = "select * from individual\n" + "where id=:id")
    Mono<Individual> getIndividualById(String id);

    @Query(
            value =
                    "select i.* " + "from sme_user.individual i "
                            + "    inner join sme_user.individual_unit_position iup on i.id = iup.individual_id "
                            + "    inner join  sme_user.positions p on iup.position_id = p.id "
                            + "where p.code = 'OWNER' "
                            + "and iup.organization_id in (select organization_id from  sme_user.individual_unit_position unit_position where individual_id = :individualId)")
    Mono<Individual> getOwnerOrganizationByIndividualId(String individualId);

    @Query(
            value = "select i.id, i.username, i.name, i.email from individual i "
                    + " join individual_unit_position iup on iup.individual_id = i.id "
                    + " and i.email = :email"
                    + " and iup.organization_id = :organizationId"
                    + " and i.status = 1 "
                    + " group by i.id ")
    Mono<Individual> findIndividualIdByEmail(String email, String organizationId);

    @Query(
            value = "select * from individual \n"
                    + "inner join individual_unit_position iup on individual.id = iup.individual_id \n"
                    + "where individual.status = 1 and iup.status = 1 and iup.organization_id =:organizationId \n"
                    + "and position_id in (select id from positions where is_system = 1 and code =:positionCode )")
    Mono<Individual> findAccountByOrgId(String organizationId, String positionCode);

    @Query(value = "select distinct(user_id) from individual where status =1 and username =:username")
    Mono<String> findUserIdFromUsername(String username);

    @Query(
            value = "with query as (select iup.individual_id\n" + "               from individual_unit_position iup\n"
                    + "                        join positions p on p.id = iup.position_id\n"
                    + "               where p.code = 'REPRESENTATIVE' and iup.organization_unit_id in (:organizationUnitIds))\n"
                    + "select i.*, iup.organization_unit_id\n"
                    + "from individual i join individual_unit_position iup on i.id = iup.individual_id\n"
                    + "where i.id in (select * from query)")
    Flux<IndividualDTO> findRepresentativeOfRep(List<String> organizationUnitIds);

    @Query(" select individual.*, iup.organization_unit_id\n" + "from individual\n"
            + "inner join individual_unit_position iup on individual.id = iup.individual_id\n"
            + "inner join positions on iup.position_id = positions.id\n"
            + "where positions.code =:positionCode and iup.organization_unit_id in (:organizationUnitIds);")
    Flux<IndividualDTO> findRepresentativeByUnitIds(List<String> organizationUnitIds, String positionCode);

    @Query(
            value = "update\n" + "   individual\n"
                    + " set\n"
                    + "   name = :name,\n"
                    + "   phone =:phone,\n"
                    + "   address =:address ,\n"
                    + "   province_code =:province,\n"
                    + "   district_code =:districtCode ,\n"
                    + "   precinct_code =:precinctCode ,\n"
                    + "   position_code =:positionCode ,\n"
                    + "   email =:email,\n"
                    + "   gender =:gender ,\n"
                    + "   birthday =:birthday,\n"
                    + "   update_at = now(),\n"
                    + "   update_by =:updateBy "
                    + " where\n"
                    + "   id =:id")
    Mono<Long> updateIndividualById(
            String id,
            String name,
            String phone,
            String address,
            String province,
            String districtCode,
            String precinctCode,
            String positionCode,
            String email,
            String gender,
            LocalDateTime birthday,
            String updateBy);

    @Query(
            "select i.* from individual i " + " join individual_unit_position iup on i.id = iup.individual_id "
                    + " join positions p on p.id = iup.position_id "
                    + " where "
                    + "  p.code = :code and p.is_system = 1 and p.status = 1"
                    + "  and i.status = :state "
                    + "  and iup.state = :state and iup.organization_id = :organizationId and iup.organization_unit_id = :organizationUnitId ")
    Mono<Individual> getIndividualInfoByPositionCode(
            String code, Integer state, String organizationUnitId, String organizationId);

    @Query("select count(*) from individual i " + "join individual_unit_position iup on i.id = iup.individual_id "
            + "  where "
            + "   i.status = :state and i.user_id is not null "
            + "   and iup.state = :state and iup.organization_id = :organizationId and iup.organization_unit_id = :organizationUnitId "
            + "   and iup.position_id not in ( "
            + " select "
            + "   id"
            + " from "
            + "   positions p2 "
            + " where "
            + "   code IN ('LEADER', 'REPRESENTATIVE') "
            + "   and is_system = 1 "
            + "   and status = 1)")
    Mono<Long> countIndividual(Integer state, String organizationUnitId, String organizationId);

    @Query(
            "select i.* from individual i " + " join individual_unit_position iup on i.id = iup.individual_id "
                    + " join positions p on p.id = iup.position_id "
                    + " where "
                    + "  p.code = :code and p.is_system = 1 and p.status = 1"
                    + "  and i.status = :state and i.user_id is not null "
                    + "  and iup.state = :state and iup.organization_id = :organizationId and iup.organization_unit_id = :organizationUnitId ")
    Mono<Individual> getLeader(String code, Integer state, String organizationId, String organizationUnitId);

    @Query(
            value =
                    "select * from individual where status in (0,1) and lower(email_account) = lower(:emailAccount) limit 1")
    Mono<Individual> findByEmailAccount(String emailAccount);

    @Query(
            value =
                    "select distinct(user_id) from individual where status in (0,1) and lower(username) = lower(:username)")
    Mono<Individual> findByUsernameIgnoreCase(String username);

    @Query(value = "select * from individual where status = 1 and code=:code")
    Mono<Individual> findIdByCode(String code);

    @Query(
            value = "select i.username,i.id\n"
                    + "from individual i\n"
                    + "INNER join individual_unit_position iup ON i.id = iup.individual_id\n"
                    + "INNER join organization_unit ou ON iup.organization_unit_id = ou.id\n"
                    + "where i.status=1\n"
                    + "and iup.status=1\n"
                    + "and lower(i.username)=lower(:username)\n"
                    + "and iup.organization_id=:organizationId\n"
                    + "and ou.code=:code;")
    Mono<Individual> findIdByUsernameWithOrganizationId(String username, String organizationId, String code);

    @Modifying
    @Query(
            value = "update\n" + "   individual\n"
                    + "set\n"
                    + "   status =-1, \n"
                    + "   update_at = now(),\n"
                    + "   update_by =:updateBy "
                    + "where\n"
                    + "   id =:id")
    Mono<Integer> deleteIndividualById(String id, String updateBy);

    @Query(value = "SELECT max(code) FROM individual WHERE code REGEXP '^[0-9]+$' and length(code) = :length")
    Mono<String> findMaxIndividualCode(int length);

    @Query(
            value = "update\n" + "   individual\n"
                    + "set\n"
                    + "   status = :status,\n"
                    + "   email =:email,\n"
                    + "   phone =:phone,\n"
                    + "   name =:name ,\n"
                    + "   gender =:gender ,\n"
                    + "   birthday =:birthday,\n"
                    + "   address =:address ,\n"
                    + "   probation_day =:probationDay, \n"
                    + "   start_working_day =:startWorkingDay, \n"
                    + "   update_at = now(),\n"
                    + "   update_by =:updateBy \n"
                    + " where\n"
                    + "   id =:id")
    Mono<Long> updateIndividualById(
            String id,
            Integer status,
            String email,
            String phone,
            String name,
            String gender,
            LocalDateTime birthday,
            String address,
            LocalDateTime probationDay,
            LocalDateTime startWorkingDay,
            String updateBy);

    @Query(
            value = "update\n" + "   individual\n"
                    + "set\n"
                    + "   user_id = :userId, \n"
                    + "   update_at = now(),\n"
                    + "   update_by =:updateBy \n"
                    + " where\n"
                    + "   id =:id")
    Mono<Integer> updateUserId(String id, String userId, String updateBy);

    @Query(
            value = "update\n" + "   individual\n"
                    + "set\n"
                    + "   image = :image, \n"
                    + "   update_at = now(),\n"
                    + "   update_by =:updateBy \n"
                    + " where\n"
                    + "   id =:id")
    Mono<Integer> updateImage(String id, String image, String updateBy);

    @Query(value = "select user_id\n" + "from individual\n" + "where individual.id =:individualId")
    Mono<String> getUserIdByIndividualId(String individualId);

    @Query(value = "select * from individual where username = :username")
    Mono<Individual> findByUsername(String username);

    @Query(value = "select * from individual where username = :username")
    Flux<Individual> findAllByUserName(String username);

    @Query(value = "select id from individual\n" + "where user_id =:userId and status =:status")
    Flux<String> getIndividualByUserId(String userId, Integer status);

    @Query(value = "select user_id\n" + "from individual\n" + "where individual.id = :individualId and status = 1")
    Mono<String> getUserIdByIndividualIdAndActive(String individualId);

    @Query(
            value = " select individual_id from "
                    + "                          (select iup.individual_id as individual_id "
                    + "        from individual_unit_position iup "
                    + "                 join individual i on i.id = iup.individual_id "
                    + "        where i.user_id = :userId "
                    + "          and iup.organization_id = :organizationId "
                    + "        limit 1 "
                    + "       ) a "
                    + " union all "
                    + " (select id as individual_id from sme_user.individual where user_id = :userId) "
                    + " limit 1")
    Mono<String> findIndividualIdByUserId(String userId, String organizationId);

    /**
     * Get userId of Owner by idNo primary_identify of organization
     *
     * @param idNo
     * @param positionCode
     * @return
     */
    @Query(
            value = "select user_id\n" + "from individual\n"
                    + "where id = (select individual_id\n"
                    + "            from individual_unit_position iup\n"
                    + "                     join positions p on iup.position_id = p.id\n"
                    + "            where p.code = :positionCode\n"
                    + "              and iup.organization_id =\n"
                    + "                  (select tenant_id from tenant_identify ti where ti.id =:tenantId)\n"
                    + "            limit 1)")
    Mono<String> getUserIdOfOwnerByIdNo(String tenantId, String positionCode);

    @Query("select nextval(sme_user.individualCodeSeq)")
    Mono<String> getIndividualCodeSeq();

    @Query(
            value =
                    "with query as (select iup.individual_id from sme_user.individual_unit_position iup inner join sme_user.positions p on iup.position_id = p.id\n"
                            + "               where  iup.organization_id = :organizationId and p.code = 'REPRESENTATIVE')\n"
                            + "            select i.*\n"
                            + "            from sme_user.individual i\n"
                            + "            where i.id = (select * from query)")
    Mono<Individual> findRepresentativeByOrganizationId(String organizationId);
}
