package com.ezbuy.ratingservice.controller;

import com.ezbuy.ratingservice.model.entity.RatingType;
import com.ezbuy.ratingservice.service.RatingTypeService;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/rating-type")
@RequiredArgsConstructor
public class RatingTypeController {
    private final RatingTypeService ratingTypeService;

    @GetMapping("/all-active")
    public Mono<DataResponse<List<RatingType>>> getAllRatingActive() {
        return ratingTypeService.getAllActive().map(DataResponse::success);
    }
}
