package com.ezbuy.settingservice.repository;

import com.ezbuy.settingservice.model.entity.NewsInfo;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NewsInfoRepository extends R2dbcRepository<NewsInfo, UUID> {
    @Query(value = "select * from news_info where id = :id")
    Mono<NewsInfo> getById(String id);

    @Query(
            value =
                    "update news_info set code = :code, title = :title, display_order = :displayOrder, status = :status, group_news_id = :groupNewsId, state = :state, summary = :summary, update_by = :user, update_at = CURRENT_TIMESTAMP(), navigator_url = :navigatorUrl where id = :id")
    Mono<NewsInfo> updateNewsInfo(
            String id,
            String code,
            String title,
            Integer displayOrder,
            Integer status,
            String groupNewsId,
            String state,
            String summary,
            String user,
            String navigatorUrl);

    @Query("select * from news_info where code = :code")
    Mono<NewsInfo> findByCode(String code);

    @Query("select * from news_info where display_order = :displayOrder and group_news_id = :groupNewsId")
    Mono<NewsInfo> findByGroupOrder(Integer groupOrder, String groupNewsId);

    @Query("select * from news_info where status = 1 ORDER BY name")
    Flux<NewsInfo> getAllNewsInfoActive();

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
