package com.ezbuy.settingservice.controller;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.request.SearchGroupNewsRequest;
import com.ezbuy.settingmodel.model.GroupNews;
import com.ezbuy.settingmodel.request.CreateGroupNewsRequest;
import com.ezbuy.settingmodel.response.SearchGroupNewsResponse;
import com.ezbuy.settingservice.service.GroupNewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(UrlPaths.GroupNews.PREFIX)
@RequiredArgsConstructor
public class GroupNewsController {
    private final GroupNewsService groupNewsService;

    @PostMapping
    public Mono<DataResponse<GroupNews>> createGroupNews(@RequestBody CreateGroupNewsRequest request) {
        return groupNewsService.createGroupNews(request);
    }

    @PutMapping(value = UrlPaths.GroupNews.UPDATE)
    public Mono<DataResponse<GroupNews>> editGroupNews(@PathVariable String id, @Valid @RequestBody CreateGroupNewsRequest request) {
        return groupNewsService.editGroupNews(id, request);
    }

    @GetMapping
    public Mono<SearchGroupNewsResponse> findGroupNews(@ModelAttribute SearchGroupNewsRequest request) {
        return groupNewsService.findGroupNews(request);
    }

    @GetMapping(UrlPaths.GroupNews.ALL)
    public Mono<DataResponse<List<GroupNews>>> getAllGroupNews() {
        return groupNewsService.getAllGroupNews()
                .map(rs -> new DataResponse<>("success", rs));
    }
}
