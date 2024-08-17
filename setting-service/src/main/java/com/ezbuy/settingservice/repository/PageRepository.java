package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.Page;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PageRepository extends R2dbcRepository<Page, String> {
    @Query("Select * from page p where p.code = :code and p.status = 1")
    Flux<Page> getPageByPageLink(String pageLink);

    @Query("select * from page p where lower(p.title) like lower(:title)")
    Flux<Page> findByTitle(String title);

    @Query("select * from page p where p.code like :code")
    Flux<Page> findByCode(String code);

    @Query("Select * from page p where p.id = :pageId and p.status = 1")
    Mono<Page> getById(String pageId);

    @Query(value = "update page set title=:title, update_by=:user, update_at=CURRENT_TIMESTAMP() where id=:pageId")
    Mono<Page> updatePage(String pageId, String title, String user);
}
