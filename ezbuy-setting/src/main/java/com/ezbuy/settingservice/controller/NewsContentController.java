package com.ezbuy.settingservice.controller;

import com.ezbuy.settingservice.constants.UrlPaths;
import com.ezbuy.settingservice.model.dto.NewsContentDTO;
import com.ezbuy.settingservice.model.entity.NewsContent;
import com.ezbuy.settingservice.model.dto.request.CreateNewsContentRequest;
import com.ezbuy.settingservice.service.NewsContentService;
import com.ezbuy.core.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.News.PREFIX)
public class NewsContentController {

    private final NewsContentService newsContentService;

    @PostMapping
    public Mono<DataResponse<NewsContent>> createNewsContent(@Valid @RequestBody CreateNewsContentRequest request) {
        return newsContentService.createNewsContent(request);
    }

    @PutMapping(UrlPaths.News.PATH_VARIABLE_ID)
    public Mono<DataResponse<NewsContent>> updateNewsContent(
            @PathVariable String id, @Valid @RequestBody CreateNewsContentRequest request) {
        return newsContentService.editNewsContent(id, request);
    }

    @GetMapping(UrlPaths.News.FIND_BY_NEWS_INFO_ID)
    public Mono<DataResponse<List<NewsContentDTO>>> findByNewsInfoId(@PathVariable String newsInfoId) {
        return newsContentService.findNewsInfoByNewsInfo(newsInfoId).map(rs -> new DataResponse<>("success", rs));
    }
}
