package com.ezbuy.ratingservice.service;

import com.ezbuy.ratingservice.model.entity.RatingType;
import java.util.List;
import reactor.core.publisher.Mono;

public interface RatingTypeService {

    Mono<List<RatingType>> getAllActive();
}
