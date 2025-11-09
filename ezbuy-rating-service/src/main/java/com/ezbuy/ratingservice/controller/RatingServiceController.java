package com.ezbuy.ratingservice.controller;

import com.ezbuy.ratingmodel.constants.UrlPaths;
import com.ezbuy.ratingmodel.dto.RatingServiceResponse;
import com.ezbuy.ratingmodel.dto.SearchRatingRequest;
import com.ezbuy.ratingservice.service.RatingService;
import com.ezbuy.core.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = UrlPaths.RatingService.PREFIX)
public class RatingServiceController {

    private final RatingService ratingService;

    @GetMapping
    public Mono<DataResponse<RatingServiceResponse>> getRatingService(
            @RequestParam("service_alias") String serviceAlias) {
        return ratingService.getRatingService(serviceAlias);
    }

    @GetMapping(value = UrlPaths.Rating.GET_RATTING_SERVICE_PAGING)
    public Mono<DataResponse<RatingServiceResponse>> getRatingByServicePaging(
            @ModelAttribute SearchRatingRequest request) {
        return ratingService.getRatingServicePaging(request);
    }
}
