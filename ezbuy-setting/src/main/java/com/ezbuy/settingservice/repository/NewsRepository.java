package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.News;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends ReactiveCrudRepository<News, String> {}
