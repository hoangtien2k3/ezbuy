package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingservice.model.dto.GroupNewsDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.dto.request.SearchGroupNewsRequest;
import com.ezbuy.settingservice.model.entity.GroupNews;
import com.ezbuy.settingservice.model.dto.request.CreateGroupNewsRequest;
import com.ezbuy.settingservice.model.dto.response.SearchGroupNewsResponse;
import com.ezbuy.settingservice.repository.GroupNewsRepository;
import com.ezbuy.settingservice.repositoryTemplate.GroupNewsRepositoryTemplate;
import com.ezbuy.settingservice.service.GroupNewsService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.constants.Constants;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GroupNewsServiceImpl extends BaseServiceHandler implements GroupNewsService {
    private final GroupNewsRepository groupNewsRepository;
    private final GroupNewsRepositoryTemplate groupNewsRepositoryTemplate;

    @Override
    @Transactional
    public Mono<DataResponse<GroupNews>> createGroupNews(CreateGroupNewsRequest request) {
        // validate input
        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());
        Integer displayOrder = request.getDisplayOrder();
        var getSysDate = groupNewsRepository.getSysDate();
        return Mono.zip(SecurityUtils.getCurrentUser().switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        validateExistGroupNews(code, displayOrder),
                        getSysDate)
                .flatMap(tuple -> {
                    String groupNewsId = UUID.randomUUID().toString();
                    LocalDateTime now = tuple.getT3();
                    String name = DataUtil.safeTrim(request.getName());
                    String userName = tuple.getT1().getUsername();
                    GroupNews groupNews = GroupNews.builder()
                            .id(groupNewsId)
                            .code(code)
                            .name(name)
                            .displayOrder(displayOrder)
                            .status(request.getStatus())
                            .createAt(now)
                            .createBy(userName)
                            .updateAt(now)
                            .updateBy(userName)
                            .build();
                    return groupNewsRepository
                            .save(groupNews)
                            .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "group.news.insert.failed")))
                            .flatMap(x -> Mono.just(new DataResponse<>("success", groupNews)));
                });
    }

    public void validateInput(CreateGroupNewsRequest request) {
        String code = DataUtil.safeTrim(request.getCode());
        if (DataUtil.isNullOrEmpty(code)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.group.news.code.empty");
        }
        if (code.length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.group.news.code.max.length");
        }
        String name = DataUtil.safeTrim(request.getName());
        if (DataUtil.isNullOrEmpty(name)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.group.news.name");
        }
        if (name.length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.group.news.name.max.length");
        }
        Integer displayOrder = request.getDisplayOrder();
        if (displayOrder == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.group.news.order");
        }
        if (displayOrder < 1) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.group.news.order.min");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.error");
        }
    }

    public Mono<Boolean> validateExistGroupNews(String code, Integer displayOrder) {
        return Mono.zip(groupNewsRepository.findByCode(code).defaultIfEmpty(new GroupNews()),
                        groupNewsRepository.findByDisplayOrder(displayOrder).defaultIfEmpty(new GroupNews()))
                .flatMap(tuple -> {
                    GroupNews groupNewsByCode = tuple.getT1();
                    if (groupNewsByCode.getCode() != null) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.NOT_FOUND, "create.group.news.code.is.exists"));
                    }
                    GroupNews groupNewsByDisplayOrder = tuple.getT2();
                    if (groupNewsByDisplayOrder.getDisplayOrder() != null) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.NOT_FOUND, "create.group.news.order.is.exits"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<GroupNews>> editGroupNews(String id, CreateGroupNewsRequest request) {
        validateInput(request);
        String groupNewsId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(groupNewsId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "group.news.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser()
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        groupNewsRepository.getById(groupNewsId)
                                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "group.news.not.found"))))
                .flatMap(tuple -> {
                    GroupNews groupNews = tuple.getT2();
                    Mono<Boolean> checkExistGroupCode = Mono.just(true);
                    Mono<Boolean> checkExistGroupOrder = Mono.just(true);
                    String code = DataUtil.safeTrim(request.getCode());
                    if (!DataUtil.safeEqual(code, groupNews.getCode())) {
                        checkExistGroupCode = validateExistGroupNews(code, null);
                    }
                    Integer displayOrder = request.getDisplayOrder();
                    if (!DataUtil.safeEqual(displayOrder, groupNews.getDisplayOrder())) {
                        checkExistGroupOrder = validateExistGroupNews(null, displayOrder);
                    }
                    String name = DataUtil.safeTrim(request.getName());
                    Mono<GroupNews> updateGroupNews = groupNewsRepository
                            .updateGroupNews(groupNewsId, code, name, displayOrder, request.getStatus(), tuple.getT1().getUsername())
                            .defaultIfEmpty(new GroupNews());
                    return Mono.zip(checkExistGroupCode, checkExistGroupOrder, updateGroupNews)
                            .flatMap(response -> Mono.just(new DataResponse<>("success", null)));
                });
    }

    @Override
    @Transactional
    public Mono<SearchGroupNewsResponse> findGroupNews(SearchGroupNewsRequest request) {
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        if ((Objects.isNull(request.getFromDate()) && Objects.nonNull(request.getToDate()))
                || (Objects.nonNull(request.getFromDate()) && Objects.isNull(request.getToDate()))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.date.request.invalid");
        }
        if (!Objects.isNull(request.getFromDate())) {
            if (request.getFromDate().isAfter(request.getToDate())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
            }
        }
        Flux<GroupNewsDTO> groupNews = groupNewsRepositoryTemplate.findGroupNews(request);
        Mono<Long> countMono = groupNewsRepositoryTemplate.countGroupNews(request);
        return Mono.zip(groupNews.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchGroupNewsResponse response = new SearchGroupNewsResponse();
            response.setLstGroupNewsDTO(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }

    @Override
    @Transactional
    public Mono<List<GroupNews>> getAllGroupNews() {
        return groupNewsRepository.getAllGroupNews().collectList();
    }
}
