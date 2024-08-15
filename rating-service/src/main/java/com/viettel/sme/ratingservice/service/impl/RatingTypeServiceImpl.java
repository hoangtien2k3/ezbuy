package com.viettel.sme.ratingservice.service.impl;

import com.ezbuy.ratingmodel.model.RatingType;
import com.viettel.sme.ratingservice.repository.RatingTypeRepository;
import com.viettel.sme.ratingservice.service.RatingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

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
