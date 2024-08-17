package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.ContentSection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ContentSectionRepository extends R2dbcRepository<ContentSection, String> {
    @Query(value = "select * from content_section where id = :id")
    Mono<ContentSection> getById(String id);

    @Query("select * from content_section order by create_at desc")
    Flux<ContentSection> findAllCS();

    Flux<ContentSection> findByRefId(String refId);

    Flux<ContentSection> findBySectionId(String sectionId);

    @Query(value = "update content_section set parent_id = :parentId," +
            " section_id= :sectionId, name = :name,display_order=:displayOrder ," +
            " status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<ContentSection> updateCS(String sectionId, String parentId, String name, Integer status, Long displayOrder, String user, String id);

    @Query(value = "select * from content_section where name = :name")
    Mono<ContentSection> findByName(String name);

    @Query("select * from content_section where status = 1")
    Flux<ContentSection> getAllActiveCS();

    @Query(value = "select * from content_section where ref_id in (:lstServiceId) and status = 1 ")
    Flux<ContentSection> getByServiceId(List<String> lstServiceId);

    // Setting-005 4/1/2024
    @Query(value = "select * from content_section where ref_alias in (:lstAlias) and status = 1 ")
    Flux<ContentSection> getByAlias(List<String> lstAlias);

    @Query(value = "select * from content_section where section_id in (:lstSectionId) and status = 1 ")
    Flux<ContentSection> getBySectionId(List<String> lstSectionId);

    @Query(value = "select * from content_section\n" +
            "where status = 1 " +
            "  and type = :type " +
            "  and ref_id = :refId " +
            "  and ref_type = :refType")
    Flux<ContentSection> findAllActiveByTypeAndRefIdAndRefTypeAndStatus(String type, String refId, String refType);

    @Query(value = "select * from content_section\n" +
            "where status = 1 " +
            "  and type = :type " +
            "  and ref_alias = :refAlias " +
            "  and ref_type = :refType")
    Flux<ContentSection> findAllActiveByTypeAndRefIdAndRefTypeAndStatusV2(String type, String refAlias, String refType);

    @Query(value = "select * from content_section\n" +
            "where status = 1 " +
            "  and type = :type " +
            "  and ref_type = :refType")
    Flux<ContentSection> findAllActiveByTypeAndRefTypeAndStatus(String type, String refType);

    @Query(value = "update content_section set " +
            " status = :status, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<Boolean> updateStatus(Integer status, String id);

    @Query("select * from content_section where id = :id and status = 1")
    Mono<ContentSection> getActiveById(String id);

    @Query("select * from content_section where parent_id = :parentId and status = 1")
    Flux<ContentSection> getAllActiveContentSectionsByParentId(String parentId);
}
