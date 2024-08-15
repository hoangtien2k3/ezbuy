package com.viettel.sme.ratingservice.controller;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.ratingmodel.constants.UrlPaths;
import com.ezbuy.ratingmodel.model.RatingType;
import com.viettel.sme.ratingservice.service.RatingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(UrlPaths.RatingType.PREFIX)
@RequiredArgsConstructor
@CrossOrigin
public class RatingTypeController {
    private final RatingTypeService ratingTypeService;

    @GetMapping(value = UrlPaths.RatingType.GET_ALL_ACTIVE)
    public Mono<DataResponse<List<RatingType>>> getAllRatingActive(){
        return ratingTypeService.getAllActive()
                .map(rs -> new DataResponse<>("success", rs));
    }
}
