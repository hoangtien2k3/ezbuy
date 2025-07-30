package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.Individual;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IndividualRepository extends R2dbcRepository<Individual, String> {
    @Query("""
            SELECT * FROM individual
            WHERE LOWER(code) = LOWER(:code)
              AND status = :status
            """)
    Mono<Individual> findByCode(String code, Integer status);

    @NotNull
    @Query("""
            SELECT * FROM individual
            WHERE status IN (0, 1)
              AND id = :id
            """)
    Mono<Individual> findById(@NotNull String id);

    @Query("""
            SELECT * FROM individual
            WHERE id = :id
              AND status = :status
            """)
    Mono<Individual> findByIdAndState(String id, Integer status);

    @Query("""
            SELECT * FROM individual
            WHERE id = :id
              AND status = :status
            """)
    Mono<Individual> getIndividualByIdAnAndStatus(String id, Integer status);

    @Query("""
            SELECT * FROM individual
            WHERE id = :id
            """)
    Mono<Individual> getIndividualById(String id);

    @Query("""
            SELECT i.id, i.username, i.name, i.email
            FROM individual i
                     JOIN individual_unit_position iup ON iup.individual_id = i.id
            WHERE i.email = :email
              AND iup.organization_id = :organizationId
              AND i.status = 1
            GROUP BY i.id
            """)
    Mono<Individual> findIndividualIdByEmail(String email, String organizationId);

    @Query("""
            SELECT COUNT(*)
            FROM individual i
                     JOIN individual_unit_position iup ON i.id = iup.individual_id
            WHERE i.status = :state
              AND i.user_id IS NOT NULL
              AND iup.state = :state
              AND iup.organization_id = :organizationId
              AND iup.organization_unit_id = :organizationUnitId
              AND iup.position_id NOT IN (
                  SELECT id FROM positions p2
                  WHERE code IN ('LEADER', 'REPRESENTATIVE')
                    AND is_system = 1
                    AND status = 1
              )
            """)
    Mono<Long> countIndividual(Integer state, String organizationUnitId, String organizationId);

    @Query("""
            SELECT * FROM individual
            WHERE status IN (0,1)
              AND LOWER(email_account) = LOWER(:emailAccount)
            LIMIT 1
            """)
    Mono<Individual> findByEmailAccount(String emailAccount);

    @Query("""
            SELECT DISTINCT(user_id)
            FROM individual
            WHERE status IN (0,1)
              AND LOWER(username) = LOWER(:username)
            """)
    Mono<Individual> findByUsernameIgnoreCase(String username);

    @Query("""
            SELECT * FROM individual
            WHERE status = 1
              AND code = :code
            """)
    Mono<Individual> findIdByCode(String code);

    @Query("""
            SELECT i.username, i.id
            FROM individual i
                     INNER JOIN individual_unit_position iup ON i.id = iup.individual_id
                     INNER JOIN organization_unit ou ON iup.organization_unit_id = ou.id
            WHERE i.status = 1
              AND iup.status = 1
              AND LOWER(i.username) = LOWER(:username)
              AND iup.organization_id = :organizationId
              AND ou.code = :code
            """)
    Mono<Individual> findIdByUsernameWithOrganizationId(String username, String organizationId, String code);

    @Modifying
    @Query("""
            UPDATE individual
            SET status = -1,
                update_at = NOW(),
                update_by = :updateBy
            WHERE id = :id
            """)
    Mono<Integer> deleteIndividualById(String id, String updateBy);

    @Query("""
            SELECT MAX(code)
            FROM individual
            WHERE code REGEXP '^[0-9]+$'
              AND LENGTH(code) = :length
            """)
    Mono<String> findMaxIndividualCode(int length);

    @Query("""
            UPDATE individual
            SET user_id = :userId,
                update_at = NOW(),
                update_by = :updateBy
            WHERE id = :id
            """)
    Mono<Integer> updateUserId(String id, String userId, String updateBy);

    @Query("""
            UPDATE individual
            SET image = :image,
                update_at = NOW(),
                update_by = :updateBy
            WHERE id = :id
            """)
    Mono<Integer> updateImage(String id, String image, String updateBy);

    @Query("""
            SELECT user_id
            FROM individual
            WHERE individual.id = :individualId
            """)
    Mono<String> getUserIdByIndividualId(String individualId);

    @Query("""
            SELECT * FROM individual
            WHERE username = :username
            """)
    Mono<Individual> findByUsername(String username);

    @Query("""
            SELECT * FROM individual
            WHERE username = :username
            """)
    Flux<Individual> findAllByUserName(String username);

}
