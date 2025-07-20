package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.Organization;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface OrganizationRepo extends R2dbcRepository<Organization, String> {

    @Query("SELECT * FROM organization WHERE id = :id AND status = :status")
    Mono<Organization> findOrganizationByIdAndStatus(String id, int status);

    @Query("""
            UPDATE organization
            SET name = :name,
                province_code = :provinceCode,
                district_code = :districtCode,
                precinct_code = :precinctCode,
                phone = :phone,
                founding_date = :foundingDate,
                business_type = :businessType,
                image = :image,
                street_block = :streetBlock,
                email = :email,
                state = IF(:state IS NULL, state, :state),
                create_at = NOW(),
                create_by = :emailToken,
                org_type = :orgType
            WHERE id = :id
            """)
    Mono<Boolean> updateOrganizationById(
            String id,
            String name,
            String provinceCode,
            String districtCode,
            String precinctCode,
            String phone,
            LocalDateTime foundingDate,
            String businessType,
            String image,
            String streetBlock,
            String email,
            Integer state,
            String emailToken,
            String orgType);

    @Query("""
            SELECT o.*
            FROM individual i
                INNER JOIN individual_unit_position iup ON i.id = iup.individual_id
                INNER JOIN positions p ON iup.position_id = p.id
                INNER JOIN organization o ON iup.organization_id = o.id
            WHERE i.id = :individualId
            """)
    Mono<Organization> getOrganizationByIndividualId(String individualId);

    @Query("""
            SELECT id FROM organization
            WHERE id IN (
                SELECT tenant_id FROM tenant_identify
                WHERE type = :type
                  AND status = 1
                  AND trust_status = 1
                  AND id_no = :idNo
            )
            """)
    Mono<String> findOrganizationByIdentify(String type, String idNo);

    @Query("""
            SELECT DISTINCT(individual_unit_position.organization_id)
            FROM individual
                INNER JOIN individual_unit_position ON individual.id = individual_unit_position.individual_id
                INNER JOIN positions ON individual_unit_position.position_id = positions.id
            WHERE individual.username = :username
              AND positions.code = :code
            """)
    Mono<String> getOrganizationIdByUsername(String username, String code);

    @Query("""
            UPDATE organization
            SET founding_date = :foundingDate,
                create_at = NOW(),
                create_by = :emailToken
            WHERE id = :id
            """)
    Mono<Boolean> updateFoundingDateById(String id, LocalDateTime foundingDate, String emailToken);
}
