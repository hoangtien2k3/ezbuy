package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.NewsDetailDTO;
import com.ezbuy.settingmodel.dto.RelateNewsDTO;
import com.ezbuy.settingmodel.dto.request.SearchNewsInfoRequest;
import com.ezbuy.settingmodel.model.NewsInfo;
import com.ezbuy.settingmodel.request.CreateNewsInfoRequest;
import com.ezbuy.settingmodel.response.SearchNewsInfoResponse;
import com.ezbuy.settingservice.service.NewsInfoService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.NewsInfo.PREFIX)
@RequiredArgsConstructor
@CrossOrigin("*")
public class NewsInfoController {
    private final NewsInfoService newsInfoService;

    @PostMapping
    public Mono<DataResponse<NewsInfo>> createServiceGroup(@RequestBody CreateNewsInfoRequest request) {
        return newsInfoService.createNewsInfo(request);
    }

    @PutMapping(value = UrlPaths.NewsInfo.UPDATE)
    public Mono<DataResponse<NewsInfo>> editServiceGroup(
            @PathVariable String id, @Valid @RequestBody CreateNewsInfoRequest request) {
        return newsInfoService.editNewsInfo(id, request);
    }

    @GetMapping
    public Mono<SearchNewsInfoResponse> findServiceGroup(@ModelAttribute SearchNewsInfoRequest request) {
        return newsInfoService.findNewsInfo(request);
    }

    @GetMapping(UrlPaths.NewsInfo.ALL)
    public Mono<DataResponse<List<NewsInfo>>> getAllServiceGroupActive() {
        return newsInfoService.getAllNewsInfoActive().map(rs -> new DataResponse<>("success", rs));
    }

    @GetMapping(value = UrlPaths.NewsInfo.DETAIL)
    public Mono<DataResponse<NewsDetailDTO>> getNewsDetailByNewsInfoId(@PathVariable String id) {
        return newsInfoService.getNewsDetailByNewsInfoId(id).map(rs -> rs);
    }

    @GetMapping(value = UrlPaths.NewsInfo.RELATE_NEWS)
    public Mono<DataResponse<List<RelateNewsDTO>>> getRelateNewsByGroupNewsId(@PathVariable String id) {
        return newsInfoService.getRelateNewsByGroupNewsId(id).map(rs -> rs);
    }
}
