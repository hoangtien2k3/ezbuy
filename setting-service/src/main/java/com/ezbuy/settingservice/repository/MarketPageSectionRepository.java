package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.MarketPageSection;
import com.ezbuy.settingmodel.model.MarketSection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketPageSectionRepository extends R2dbcRepository<MarketPageSection, String> {
    @Query(value = "select * from market_page_section where page_id = :pageId")
    Flux<MarketPageSection> findByPageId(String pageId);

    @Query(value = "select * from market_page_section where id = :id")
    Mono<MarketPageSection> getById(String id);

    @Query(value = "select * from market_page_section where id = :id and (:status is null or status = :status)")
    Mono<MarketPageSection> findMarketPageSectionById(String id, String status);

    @Query(value = "update market_page_section set status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<MarketSection> updateMarketPageSectionByStatus(String id, Integer status, String user);
}
