package com.ezbuy.ratingservice.controller;

import com.ezbuy.ratingservice.model.entity.Rating;
import com.ezbuy.ratingservice.model.entity.RatingCount;
import com.ezbuy.ratingservice.model.dto.request.FindRatingRequest;
import com.ezbuy.ratingservice.model.dto.request.RatingRequest;
import com.ezbuy.ratingservice.model.dto.response.SearchRatingResponse;
import com.ezbuy.ratingservice.service.RatingService;
import com.ezbuy.core.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/rating")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/all-active")
    public Mono<DataResponse<List<RatingCount>>> getAllRatingActive() {
        return ratingService.getAllRatingActive().map(rs -> new DataResponse<>("success", rs));
    }

    @PostMapping("/rating-service")
    public Mono<DataResponse<List<RatingCount>>> getRatingByServiceId(@RequestBody List<String> alias) {
        return ratingService.getRatingByAlias(alias).map(rs -> new DataResponse<>("success", rs));
    }

    @PostMapping
    public Mono<DataResponse<Rating>> createRating(@RequestBody RatingRequest request) {
        return ratingService.createRating(request);
    }

    @PutMapping("/{id}")
    public Mono<DataResponse<Rating>> editRating(@PathVariable String id, @Valid @RequestBody RatingRequest request) {
        return ratingService.editRating(id, request);
    }

    @GetMapping("/search")
    public Mono<SearchRatingResponse> findRating(FindRatingRequest request) {
        return ratingService.findRating(request);
    }

    @GetMapping("/all")
    public Mono<DataResponse<List<RatingCount>>> getAllServiceGroupActive() {
        return ratingService.getAllRatingActive().map(DataResponse::success);
    }
}
