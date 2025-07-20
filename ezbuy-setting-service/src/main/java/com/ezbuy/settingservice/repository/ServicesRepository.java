package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.Services;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends ReactiveCrudRepository<Services, String> {}
