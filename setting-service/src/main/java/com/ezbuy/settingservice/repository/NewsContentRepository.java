package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.dto.NewsContentDTO;
import com.ezbuy.settingmodel.model.NewsContent;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface NewsContentRepository extends R2dbcRepository<NewsContent, String> {
    @Query(value = "select * from news_content where id = :id")
    Mono<NewsContent> getById(String id);

    @Query(value = "update news_content set content = :content, status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<NewsContent> updateNewsInfo(String id, String content, Integer status, String user);

    @Query("select * from news_content where news_info_id = :newsInfoId")
    Flux<NewsContentDTO> findByNewsInfoId(String newsInfoId);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
