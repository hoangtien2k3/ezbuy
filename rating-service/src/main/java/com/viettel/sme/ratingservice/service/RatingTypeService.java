package com.viettel.sme.ratingservice.service;

import com.ezbuy.ratingmodel.model.RatingType;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RatingTypeService {

    Mono<List<RatingType>> getAllActive();
}
