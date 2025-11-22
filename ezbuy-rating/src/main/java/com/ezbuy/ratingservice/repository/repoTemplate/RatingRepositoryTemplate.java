package com.ezbuy.ratingservice.repository.repoTemplate;

import com.ezbuy.ratingservice.model.dto.RatingDTO;
import com.ezbuy.ratingservice.model.dto.request.FindRatingRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingRepositoryTemplate {
    Flux<RatingDTO> findRating(FindRatingRequest request);

    Mono<Long> countServiceRating(FindRatingRequest request);
}
