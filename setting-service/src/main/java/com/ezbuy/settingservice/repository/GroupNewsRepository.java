package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.GroupNews;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GroupNewsRepository extends R2dbcRepository<GroupNews, UUID> {
    @Query(value = "select * from group_news where id = :id")
    Mono<GroupNews> getById(String id);

    @Query(
            value =
                    "update group_news set code = :code, name = :name, display_order = :displayOrder, status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<GroupNews> updateGroupNews(
            String id, String code, String name, Integer displayOrder, Integer status, String user);

    @Query("select * from group_news where code = :code")
    Mono<GroupNews> findByCode(String code);

    @Query("select * from group_news where display_order = :displayOrder")
    Mono<GroupNews> findByDisplayOrder(Integer groupOrder);

    @Query("select * from group_news ORDER BY name")
    Flux<GroupNews> getAllGroupNews();

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
