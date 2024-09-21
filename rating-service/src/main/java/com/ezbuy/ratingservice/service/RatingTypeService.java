package com.ezbuy.ratingservice.service;

import com.ezbuy.ratingmodel.model.RatingType;
import java.util.List;
import reactor.core.publisher.Mono;

public interface RatingTypeService {

    Mono<List<RatingType>> getAllActive();
}
