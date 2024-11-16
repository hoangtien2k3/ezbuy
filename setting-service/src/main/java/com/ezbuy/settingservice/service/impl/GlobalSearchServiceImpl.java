package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.request.GlobalSearchSyncRequest;
import com.ezbuy.settingmodel.model.News;
import com.ezbuy.settingmodel.model.Services;
import com.ezbuy.settingservice.repository.ContentDisplayRepository;
import com.ezbuy.settingservice.repository.NewsRepository;
import com.ezbuy.settingservice.repository.ServicesRepository;
import com.ezbuy.settingservice.service.GlobalSearchService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GlobalSearchServiceImpl implements GlobalSearchService {

    private final ContentDisplayRepository contentDisplayRepository;

    @Autowired
    private ApplicationContext context;

    @Override
    public Mono<DataResponse> syncService(GlobalSearchSyncRequest request) {

        long duration = 86400L;
        if (!DataUtil.isNullOrEmpty(request)) {
            duration = request.getDuration() * 60;
        }

        if (request.getDuration() < 0) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "duration.invalid"));
        }

        return Mono.from(contentDisplayRepository
                .getContentDisplayAndCode(duration)
                .collectList()
                .flatMapMany(r -> {
                    List<Services> servicesList = new ArrayList<>();
                    for (Services service : r) {
                        if (!DataUtil.isNullOrEmpty(service.getTitle())) {
                            service.setTitle(
                                    Jsoup.parseBodyFragment(service.getTitle()).text());
                        }
                        if (!DataUtil.isNullOrEmpty(service.getContent())) {
                            service.setContent(Jsoup.parseBodyFragment(service.getContent())
                                    .text());
                        }
                        servicesList.add(service);
                    }
                    log.info("contentDisplayRepository count {}", r.size());
                    try {
                        Object servicesRepositoryBean = context.getBean(ServicesRepository.class);
                        var servicesRepository = (ServicesRepository) servicesRepositoryBean;
                        return servicesRepository.saveAll(servicesList).collectList();
                    } catch (Exception ex) {
                        log.error("Get ServicesRepository bean error {}", ex);
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "error"));
                    }
                })
                .flatMap(rs -> Mono.just(new DataResponse<>("success", rs.size()))));
    }

    @Override
    public Mono<DataResponse> syncNews(GlobalSearchSyncRequest request) {

        long duration = 86400L;
        if (!DataUtil.isNullOrEmpty(request)) {
            duration = request.getDuration() * 60;
        }

        if (request.getDuration() < 0) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "duration.invalid"));
        }

        return Mono.from(contentDisplayRepository
                .getNews(duration)
                .collectList()
                .flatMapMany(r -> {
                    List<News> newsList = new ArrayList<>();
                    for (News news : r) {
                        if (!DataUtil.isNullOrEmpty(news.getTitle())) {
                            news.setTitle(
                                    Jsoup.parseBodyFragment(news.getTitle()).text());
                        }
                        if (!DataUtil.isNullOrEmpty(news.getContent())) {
                            news.setContent(
                                    Jsoup.parseBodyFragment(news.getContent()).text());
                        }
                        newsList.add(news);
                    }
                    log.info("contentDisplayRepository count {}", r.size());
                    Object newsRepositoryBean = context.getBean(NewsRepository.class);
                    var newsRepository = (NewsRepository) newsRepositoryBean;
                    return newsRepository.saveAll(newsList).collectList();
                })
                .flatMap(rs -> Mono.just(new DataResponse<>("success", rs.size()))));
    }
}
