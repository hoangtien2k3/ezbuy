package com.ezbuy.ratingservice.service.impl;

import com.ezbuy.ratingservice.model.entity.RatingType;
import com.ezbuy.ratingservice.repository.RatingTypeRepository;
import com.ezbuy.ratingservice.service.RatingTypeService;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingTypeServiceImpl extends BaseServiceHandler implements RatingTypeService {

    private final RatingTypeRepository ratingTypeRepository;

    @Override
    public Mono<List<RatingType>> getAllActive() {
        return ratingTypeRepository.getAllActive().collectList();
    }
}
