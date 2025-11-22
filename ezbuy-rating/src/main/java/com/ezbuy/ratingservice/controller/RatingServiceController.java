package com.ezbuy.ratingservice.controller;

import com.ezbuy.ratingservice.model.dto.RatingServiceResponse;
import com.ezbuy.ratingservice.model.dto.SearchRatingRequest;
import com.ezbuy.ratingservice.service.RatingService;
import com.ezbuy.core.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/rating")
public class RatingServiceController {

    private final RatingService ratingService;

    @GetMapping
    public Mono<DataResponse<RatingServiceResponse>> getRatingService(
            @RequestParam("service_alias") String serviceAlias) {
        return ratingService.getRatingService(serviceAlias);
    }

    @GetMapping("/rating-service-paging")
    public Mono<DataResponse<RatingServiceResponse>> getRatingByServicePaging(
            @ModelAttribute SearchRatingRequest request) {
        return ratingService.getRatingServicePaging(request);
    }
}
