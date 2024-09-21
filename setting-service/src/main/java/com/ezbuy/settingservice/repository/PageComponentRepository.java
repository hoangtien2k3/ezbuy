package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.PageComponent;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface PageComponentRepository extends R2dbcRepository<PageComponent, String> {
    @Query("select * from page_component where page_id = :pageId and status = 1")
    Flux<PageComponent> findByPageId(String pageId);

    @Query(
            value =
                    "update page_component set status = 0, update_by=:user, update_at=CURRENT_TIMESTAMP()  where id in (:pageComponentIds) ")
    Flux<PageComponent> deleteOldComponent(List<String> pageComponentIds, String user);
}
