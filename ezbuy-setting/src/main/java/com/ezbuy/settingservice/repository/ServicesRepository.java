package com.ezbuy.settingservice.repository;

import com.ezbuy.settingservice.model.entity.Services;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends ReactiveCrudRepository<Services, String> {}
