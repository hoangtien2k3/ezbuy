package com.ezbuy.auth.repository;

import com.nimbusds.openid.connect.sdk.assurance.evidences.Organization;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OrganizationRepo extends R2dbcRepository<Organization, UUID> {

    @Query(value = "select * from organization where id=:id and status=:status")
    Mono<Organization> findOrganizationByIdAndStatus(String id, int status);

    @Query(value = "update organization\n" +
            "set name=:name, province_code=:provinceCode, district_code=:districtCode, precinct_code=:precinctCode,\n" +
            "    phone=:phone, founding_date=:foundingDate, business_type=:businessType, image=:image,\n" +
            "    street_block=:streetBlock, email=:email, state= IF(:state is null, state, :state),\n" +
            "    create_at=NOW(), create_by=:emailToken, org_type=:orgType \n" +
            "where id=:id")
    Mono<Boolean> updateOrganizationById(String id, String name, String provinceCode, String districtCode, String precinctCode,
                                         String phone, LocalDateTime foundingDate, String businessType, String image,
                                         String streetBlock, String email, Integer state, String emailToken, String orgType);

    @Query(value = "select o.* " +
            " from sme_user.individual i " +
            "    inner join sme_user.individual_unit_position iup on i.id = iup.individual_id " +
            "    inner join  sme_user.positions p on iup.position_id = p.id " +
            "    inner join  sme_user.organization o on iup.organization_id = o.id " +
            " where i.id = :individualId ")
    Mono<Organization> getOrganizationByIndividualId(String individualId);


    @Query(value = "select id from organization\n" +
            "where id in ( \n" +
            "    select tenant_id from tenant_identify where type =:type\n" +
            "    and status = 1 and trust_status = 1 and id_no =:idNo\n" +
            ");")
    Mono<String> findOrganizationByIdentify(String type, String idNo);

    @Query(value = "select distinct(individual_unit_position.organization_id)\n" +
            "from individual\n" +
            "inner join individual_unit_position on individual.id = individual_unit_position.individual_id\n" +
            "inner join positions on individual_unit_position.position_id = positions.id\n" +
            "where individual.username =:username  and positions.code =:code")
    Mono<String> getOrganizationIdByUsername(String username, String code);

    @Query(value = "update organization\n" +
            "set  founding_date=:foundingDate, \n" +
            "    create_at=NOW(), create_by=:emailToken \n" +
            "where id=:id")
    Mono<Boolean> updateFoundingDateById(String id, LocalDateTime foundingDate, String emailToken);
}
