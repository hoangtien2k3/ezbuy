package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.ServiceMedia;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ServiceMediaRepository extends R2dbcRepository<ServiceMedia, String> {}
