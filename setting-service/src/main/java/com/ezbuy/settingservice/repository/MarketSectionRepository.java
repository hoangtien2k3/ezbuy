package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.MarketSection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketSectionRepository extends R2dbcRepository<MarketSection, String> {
    @Query(
            value = "select ms.* from sme_setting.market_page_section mps\n"
                    + "inner join sme_setting.market_page mp on mp.id = mps.page_id\n"
                    + "inner join sme_setting.market_section ms on ms.id = mps.section_id\n" + "where 1=1\n"
                    + "and mp.code = :pageCode\n" + "and mp.service_id = :serviceId\n" + "and ms.status = 1\n"
                    + "and mps.status = 1\n" + "and mp.status = 1\n" + "order by mps.display_order asc")
    Flux<MarketSection> getMarketSection(String pageCode, String serviceId);

    // bo sung truy van theo serviceAlias
    @Query(
            value = "select ms.* from sme_setting.market_page_section mps\n"
                    + "inner join sme_setting.market_page mp on mp.id = mps.page_id\n"
                    + "inner join sme_setting.market_section ms on ms.id = mps.section_id\n" + "where 1=1\n"
                    + "and mp.code = :pageCode\n" + "and mp.service_alias = :serviceAlias\n" + "and ms.status = 1\n"
                    + "and mps.status = 1\n" + "and mp.status = 1\n" + "order by mps.display_order asc")
    Flux<MarketSection> getMarketSectionV2(String pageCode, String serviceAlias);

    @Query("Select * from market_section where id = :id and (:status is null or status = :status)")
    Mono<MarketSection> findMarketSectionById(String id, Integer status);

    @Query(
            value =
                    "update market_section set type = :type, code = :code, name = :name, description = :description,"
                            + " display_order = :displayOrder, data = :data, status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<MarketSection> editMarketSection(
            String id,
            String type,
            String code,
            String name,
            String description,
            Long displayOrder,
            String data,
            Integer status,
            String user);

    @Query("select * from market_section where code = :code")
    Mono<MarketSection> getByCode(String code);

    @Query("select * from market_section where display_order = :displayOrder")
    Mono<MarketSection> getByDisplayOrder(Long groupOrder);

    @Query(
            value =
                    "update market_section set status=:status, update_by=:user, update_at=CURRENT_TIMESTAMP() where id=:id")
    Mono<MarketSection> updateStatus(String id, Integer status, String user);

    @Query("select * from market_section where status = 1")
    Flux<MarketSection> getAllActiveMarketSections();

    @Query("select * from market_section")
    Flux<MarketSection> getAllMarketSections();

    @Query("select * from market_section " + "where status = 1 "
            + "and id in (select section_id from content_section where status = 1 and id = :id)")
    Mono<MarketSection> findMarketSectionByContentSectionId(String id);

    @Query(
            "select id, type, code, name, description, display_order, status, create_by, create_at, update_at, update_by from sme_setting.market_section where status = 1 and type = 'RICH_TEXT'")
    Flux<MarketSection> getAllActiveMarketSectionsByTypeRichText();
}
