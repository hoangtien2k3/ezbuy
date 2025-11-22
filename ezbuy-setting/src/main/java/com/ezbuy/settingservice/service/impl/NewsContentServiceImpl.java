package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingservice.model.dto.NewsContentDTO;
import com.ezbuy.settingservice.model.entity.NewsContent;
import com.ezbuy.settingservice.model.dto.request.CreateNewsContentRequest;
import com.ezbuy.settingservice.repository.NewsContentRepository;
import com.ezbuy.settingservice.service.NewsContentService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NewsContentServiceImpl implements NewsContentService {

    private final NewsContentRepository newsContentRepository;

    @Override
    public Mono<DataResponse<NewsContent>> createNewsContent(CreateNewsContentRequest request) {
        var getSysDate = newsContentRepository.getSysDate();
        return Mono.zip(SecurityUtils.getCurrentUser().switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))), getSysDate)
                .flatMap(tuple -> {
                            String groupNewsId = UUID.randomUUID().toString();
                            LocalDateTime now = tuple.getT2();
                            String content = DataUtil.safeTrim(request.getContent());
                            String userName = tuple.getT1().getUsername();
                            NewsContent newsContent = NewsContent.builder()
                                    .id(groupNewsId)
                                    .content(content)
                                    .status(request.getStatus())
                                    .newsInfoId(request.getNewsInfoId())
                                    .createAt(now)
                                    .createBy(userName)
                                    .updateAt(now)
                                    .updateBy(userName)
                                    .build();
                            return newsContentRepository
                                    .save(newsContent)
                                    .switchIfEmpty(Mono.error(new BusinessException(
                                            CommonErrorCode.INTERNAL_SERVER_ERROR, "news.content.insert.failed")))
                                    .flatMap(x -> Mono.just(new DataResponse<>("success", newsContent)));
                        });
    }

    @Override
    public Mono<DataResponse<NewsContent>> editNewsContent(String id, CreateNewsContentRequest request) {
        String newsContentId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(newsContentId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "news.content.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser().switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        newsContentRepository
                                .getById(newsContentId)
                                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "news.content.not.found"))))
                .flatMap(tuple -> {
                    String content = DataUtil.safeTrim(request.getContent());
                    return newsContentRepository
                            .updateNewsInfo(
                                    newsContentId,
                                    content,
                                    request.getStatus(),
                                    tuple.getT1().getUsername())
                            .defaultIfEmpty(new NewsContent())
                            .flatMap(response -> Mono.just(new DataResponse<>("success", null)));
                });
    }

    @Override
    public Mono<List<NewsContentDTO>> findNewsInfoByNewsInfo(String newsInfoId) {
        return newsContentRepository.findByNewsInfoId(newsInfoId).collectList();
    }
}
