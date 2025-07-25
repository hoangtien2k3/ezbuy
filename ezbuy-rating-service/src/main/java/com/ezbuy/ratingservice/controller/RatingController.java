package com.ezbuy.ratingservice.controller;

import com.ezbuy.ratingmodel.constants.UrlPaths;
import com.ezbuy.ratingmodel.model.Rating;
import com.ezbuy.ratingmodel.model.RatingCount;
import com.ezbuy.ratingmodel.request.FindRatingRequest;
import com.ezbuy.ratingmodel.request.RatingRequest;
import com.ezbuy.ratingmodel.response.SearchRatingResponse;
import com.ezbuy.ratingservice.service.RatingService;
import com.reactify.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.Rating.PREFIX)
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping(value = UrlPaths.Rating.GET_ALL_RATTING_ACTIVE)
    public Mono<DataResponse<List<RatingCount>>> getAllRatingActive() {
        return ratingService.getAllRatingActive().map(rs -> new DataResponse<>("success", rs));
    }

    @PostMapping(value = UrlPaths.Rating.GET_RATTING_SERVICE)
    public Mono<DataResponse<List<RatingCount>>> getRatingByServiceId(@RequestBody List<String> alias) {
        return ratingService.getRatingByAlias(alias).map(rs -> new DataResponse<>("success", rs));
    }

    @PostMapping
    public Mono<DataResponse<Rating>> createRating(@RequestBody RatingRequest request) {
        return ratingService.createRating(request);
    }

    @PutMapping(value = UrlPaths.Rating.UPDATE)
    public Mono<DataResponse<Rating>> editRating(@PathVariable String id, @Valid @RequestBody RatingRequest request) {
        return ratingService.editRating(id, request);
    }

    @GetMapping(value = UrlPaths.Rating.SEARCH)
    public Mono<SearchRatingResponse> findRating(FindRatingRequest request) {
        return ratingService.findRating(request);
    }

    @GetMapping(UrlPaths.Rating.ALL)
    public Mono<DataResponse<List<RatingCount>>> getAllServiceGroupActive() {
        return ratingService.getAllRatingActive().map(DataResponse::success);
    }
}
