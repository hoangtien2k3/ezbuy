package com.ezbuy.settingservice.controller;

import com.ezbuy.settingservice.constants.UrlPaths;
import com.ezbuy.settingservice.model.dto.request.SearchGroupNewsRequest;
import com.ezbuy.settingservice.model.entity.GroupNews;
import com.ezbuy.settingservice.model.dto.request.CreateGroupNewsRequest;
import com.ezbuy.settingservice.model.dto.response.SearchGroupNewsResponse;
import com.ezbuy.settingservice.service.GroupNewsService;
import com.ezbuy.core.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<DataResponse<GroupNews>> editGroupNews(
            @PathVariable String id, @Valid @RequestBody CreateGroupNewsRequest request) {
        return groupNewsService.editGroupNews(id, request);
    }

    @GetMapping
    public Mono<SearchGroupNewsResponse> findGroupNews(@ModelAttribute SearchGroupNewsRequest request) {
        return groupNewsService.findGroupNews(request);
    }

    @GetMapping(UrlPaths.GroupNews.ALL)
    public Mono<DataResponse<List<GroupNews>>> getAllGroupNews() {
        return groupNewsService.getAllGroupNews().map(rs -> new DataResponse<>("success", rs));
    }
}
