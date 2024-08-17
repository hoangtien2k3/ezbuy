package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.ContentDisplay;
import com.ezbuy.settingmodel.model.News;
import com.ezbuy.settingmodel.model.Services;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ContentDisplayRepository extends R2dbcRepository<ContentDisplay, String> {
    @Query("Select cd.* from content_display cd " +
            "where cd.status = 1 and " +
            "cd.page_id = :pageId " +
            "order by display_order asc")
    Flux<ContentDisplay> getContentDisplayByPage(String pageId);

    @Query(value = "update content_display set status = 0, update_by=:user, update_at=CURRENT_TIMESTAMP()  where id in (:contentIds)")
    Flux<ContentDisplay> deleteOldContents(List<String> contentIds, String user);

    @Query("select cd.id, cd.title, cd.subtitle, cd.content, cd.update_at, p.code from content_display cd\n" +
            "inner join page p on cd.page_id = p.id\n" +
            "where (NOW() - cd.update_at) < :duration")
    Flux<Services> getContentDisplayAndCode(Long duration);

    @Query("select * from content_news cn\n" +
            "where (NOW() - cn.update_at) < :duration")
    Flux<News> getNews(Long duration);
}
