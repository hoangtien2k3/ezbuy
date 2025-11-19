package com.ezbuy.ratingservice.repository.repoTemplate;

import com.ezbuy.ratingmodel.dto.RatingDTO;
import com.ezbuy.ratingmodel.request.FindRatingRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingRepositoryTemplate {
    Flux<RatingDTO> findRating(FindRatingRequest request);

    Mono<Long> countServiceRating(FindRatingRequest request);
}
