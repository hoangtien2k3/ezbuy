package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.ContentSectionDTO;
import com.ezbuy.settingmodel.dto.ContentSectionDetailDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.dto.TreeDataDTO;
import com.ezbuy.settingmodel.dto.request.GetContentSectionRequest;
import com.ezbuy.settingmodel.model.ContentSection;
import com.ezbuy.settingmodel.request.ContentSectionRequest;
import com.ezbuy.settingmodel.request.SearchContentSectionRequest;
import com.ezbuy.settingmodel.response.SearchContentSectionResponse;
import com.ezbuy.settingservice.repository.ContentSectionRepository;
import com.ezbuy.settingservice.repositoryTemplate.ContentSectionRepositoryTemplate;
import com.ezbuy.settingservice.service.ContentSectionService;
import com.ezbuy.settingservice.service.MarketSectionService;
import com.ezbuy.settingservice.service.TelecomService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ContentSectionServiceImpl extends BaseServiceHandler implements ContentSectionService {

    private final ContentSectionRepository contentSectionRepository;
    private final ContentSectionRepositoryTemplate contentSectionRepositoryTemplate;
    private final TelecomService telecomService;
    private final MarketSectionService sectionService;

    @Override
    @Transactional
    public Mono<SearchContentSectionResponse> search(SearchContentSectionRequest request) {
        int pageIndex = DataUtil.safeToInt(request.getPageIndex(), 1);
        int pageSize = DataUtil.safeToInt(request.getPageSize(), 10);
        if (pageIndex < 1) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "pageIndex.invalid"));
        }
        if (pageSize > 100) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "pageSize.invalid"));
        }
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        Flux<ContentSectionDTO> pages = contentSectionRepositoryTemplate.queryList(request);
        Mono<Long> countMono = contentSectionRepositoryTemplate.count(request);
        return Mono.zip(pages.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex((request.getPageIndex()));
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());
            SearchContentSectionResponse response = new SearchContentSectionResponse();
            response.setContentSection(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }

    @Override
    @Transactional
    public Mono<ContentSectionDetailDTO> getContentSectionDetail(String id) {
        return contentSectionRepositoryTemplate
                .getDetailContentSection(id)
                .collectList()
                .flatMap(contentSectionList -> {
                    if (DataUtil.isNullOrEmpty(contentSectionList)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "content.section.null"));
                    } else {
                        return Mono.just(contentSectionList.getFirst());
                    }
                });
    }

    @Override
    @Transactional
    public Mono<List<ContentSection>> getAllCS() {
        return contentSectionRepository.findAllCS().collectList();
    }

    @Override
    @Transactional
    public Mono<DataResponse<ContentSection>> getCS(String id) {
        return contentSectionRepository
                .getById(id)
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "content.section.not.found")))
                .map(marketPage -> new DataResponse<>(Translator.toLocale("Success"), marketPage));
    }

    @Override
    @Transactional
    public Mono<DataResponse<ContentSection>> createCS(ContentSectionRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String parentId = DataUtil.isNullOrEmpty(request.getParentId()) ? null : request.getParentId();
        Long order = DataUtil.isNullOrEmpty(request.getDisplayOrder()) ? null : request.getDisplayOrder();
        String contentSectionId = UUID.randomUUID().toString();
        return validateContentSection(request, true).flatMap(validate -> SecurityUtils.getCurrentUser()
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "content.section.not.found")))
                .flatMap(tokenUser -> {
                    ContentSection contentSection = ContentSection.builder()
                            .id(contentSectionId)
                            .parentId(parentId)
                            .sectionId(request.getSectionId())
                            .type(request.getType())
                            .refId(request.getRefId())
                            .refType(request.getRefType())
                            .name(DataUtil.safeTrim(request.getName()))
                            .status(request.getStatus())
                            .displayOrder(order)
                            .path(null)
                            .createBy(tokenUser.getUsername())
                            .createAt(now)
                            .updateBy(tokenUser.getUsername())
                            .updateAt(now)
                            .refAlias(request.getRefAlias()) // add alias to insert
                            // PYCXXX/LuongToanTrinhScontract
                            .build();
                    return contentSectionRepository
                            .save(contentSection)
                            .doOnError(throwable -> {
                                log.error(throwable.getMessage());
                                throw new BusinessException(
                                        CommonErrorCode.BAD_REQUEST, "Create.content.Section.error");
                            })
                            .flatMap(result -> Mono.just(new DataResponse<>("success", result)));
                }));
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return contentSectionRepository
                .getById(DataUtil.safeTrim(id))
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "Content.Section.notfound")))
                .flatMap(contentSection -> {
                    contentSection.setStatus(Constants.STATUS.INACTIVE);
                    return contentSectionRepository.updateStatus(Constants.STATUS.INACTIVE, contentSection.getId());
                })
                .doOnError(throwable -> {
                    log.error(throwable.getMessage());
                    throw new BusinessException(CommonErrorCode.BAD_REQUEST, "Content.Section.notfound");
                })
                .switchIfEmpty(Mono.just(Boolean.TRUE));
    }

    @Override
    @Transactional
    public Mono<DataResponse<ContentSection>> updateCS(ContentSectionRequest request) {
        String id = request.getId();
        String parentId = DataUtil.isNullOrEmpty(request.getParentId()) ? null : request.getParentId();
        return validateContentSection(request, false).flatMap(validate -> SecurityUtils.getCurrentUser()
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "content.section.not.found")))
                .flatMap(tokenUser -> contentSectionRepository
                        .updateCS(
                                request.getSectionId(),
                                parentId,
                                request.getName(),
                                request.getStatus(),
                                request.getDisplayOrder(),
                                tokenUser.getUsername(),
                                id)
                        .defaultIfEmpty(new ContentSection())
                        .map(updatedContentSection -> new DataResponse<>("success", updatedContentSection))));
    }

    private Mono<Boolean> validateContentSection(ContentSectionRequest contentSectionRequest, boolean isInsert) {
        if (DataUtil.isNullOrEmpty(contentSectionRequest.getSectionId())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "content.section.section.id.not.empty"));
        }
        if (DataUtil.isNullOrEmpty(contentSectionRequest.getName())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "content.section.name.not.empty"));
        }
        if (DataUtil.isNullOrEmpty(contentSectionRequest.getStatus())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "content.section.status.not.empty"));
        }
        if (contentSectionRequest.getDisplayOrder() == null) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "content.section.display.order.not.empty"));
        }
        if (!DataUtil.isNullOrEmpty(contentSectionRequest.getParentId())) {
            return contentSectionRepository
                    .getActiveById(contentSectionRequest.getParentId())
                    .switchIfEmpty(Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS, "content.section.parent.id.empty.or.inactive")))
                    .flatMap(data -> contentSectionRepository
                            .getAllActiveContentSectionsByParentId(contentSectionRequest.getParentId())
                            .collectList()
                            .flatMap(contentSectionList -> {
                                if (DataUtil.isNullOrEmpty(contentSectionList)) {
                                    return Mono.just(true);
                                }
                                ContentSection contentSection = ObjectMapperFactory.getInstance()
                                        .convertValue(contentSectionRequest, ContentSection.class);
                                if (!isInsert) {
                                    contentSectionList.removeIf(
                                            cs -> DataUtil.safeEqual(cs.getId(), contentSection.getId()));
                                }
                                boolean displayOrderExists = contentSectionList.stream()
                                        .anyMatch(cs ->
                                                Objects.equals(cs.getDisplayOrder(), contentSection.getDisplayOrder()));
                                if (displayOrderExists) {
                                    return Mono.error(new BusinessException(
                                            CommonErrorCode.INVALID_PARAMS, "content.section.display.order.exist"));
                                }
                                return Mono.just(true);
                            }));
        }
        return Mono.just(true);
    }

    @Override
    public Mono<List<ContentSection>> getAllActiveCS() {
        return contentSectionRepository.getAllActiveCS().collectList().map(list -> list);
    }

    public Mono<DataResponse<List<ContentSection>>> getCSByServiceId(List<String> lstServiceId) {
        return contentSectionRepository
                .getByServiceId(lstServiceId)
                .collectList()
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "content.section.not.found")))
                .map(contentSection -> new DataResponse<>(Translator.toLocale("Success"), contentSection));
    }

    // ham lay danh sach cau hinh huong dan va tai nguyen
    @Override
    public Mono<DataResponse<List<ContentSection>>> getCSByServiceIdV2(List<String> lstAlias) {
        return contentSectionRepository
                .getByAlias(lstAlias)
                .collectList()
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "content.section.not.found")))
                .map(contentSection -> new DataResponse<>(Translator.toLocale("Success"), contentSection));
    }

    public Mono<DataResponse<List<ContentSection>>> getCSBySectionId(List<String> lstSectionId) {
        return contentSectionRepository
                .getBySectionId(lstSectionId)
                .collectList()
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "content.section.not.found")))
                .map(contentSection -> new DataResponse<>(Translator.toLocale("Success"), contentSection));
    }

    private Mono<List<TreeDataDTO>> mapGetAllData(List<ContentSection> contentSectionList) {
        List<TreeDataDTO> result = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(contentSectionList)) {
            return Mono.just(result);
        }
        contentSectionList.sort(Comparator.comparing(ContentSection::getDisplayOrder));
        // lay danh sach dieu khoan goc (khong co parent id)
        for (ContentSection contentSection : contentSectionList) {
            if (DataUtil.isNullOrEmpty(contentSection.getParentId())) {
                TreeDataDTO treeData = new TreeDataDTO();
                treeData.setChildren(new ArrayList<>());
                treeData.setKey(contentSection.getId());
                treeData.setTitle(contentSection.getName());
                result.add(treeData);
            }
        }
        // lay danh sach dieu khoan con
        for (TreeDataDTO data : result) {
            data.setChildren(handleChildrenContent(data, contentSectionList));
        }
        return Mono.just(result);
    }

    @Override
    public Mono<List<TreeDataDTO>> getAllByTypeAndRefIdAndRefType(GetContentSectionRequest request) {
        if (request == null) {
            return Mono.just(new ArrayList<>());
        }

        // uu tien refAlias prior to refId (truy van theo alias truoc roi moi den refId
        // theo luong cu)
        if (!DataUtil.isNullOrEmpty(request.getRefAlias()) && !"null".equals(request.getRefAlias())) {
            return contentSectionRepository
                    .findAllActiveByTypeAndRefIdAndRefTypeAndStatusV2(
                            request.getType(), request.getRefAlias(), request.getRefType())
                    .collectList()
                    .flatMap(this::mapGetAllData);
        } else if (!DataUtil.isNullOrEmpty(request.getRefId()) && !"null".equals(request.getRefId())) {
            return contentSectionRepository
                    .findAllActiveByTypeAndRefIdAndRefTypeAndStatus(
                            request.getType(), request.getRefId(), request.getRefType())
                    .collectList()
                    .flatMap(this::mapGetAllData);
        } else {
            return contentSectionRepository
                    .findAllActiveByTypeAndRefTypeAndStatus(request.getType(), request.getRefType())
                    .collectList()
                    .flatMap(this::mapGetAllData);
        }
    }

    /**
     * ham de quy thuc hien lay danh sach cac node con theo node cha
     */
    private List<TreeDataDTO> handleChildrenContent(TreeDataDTO dataNode, List<ContentSection> contentSectionList) {
        if (dataNode == null || DataUtil.isNullOrEmpty(contentSectionList)) {
            return new ArrayList<>();
        }
        List<TreeDataDTO> childNodeList = new ArrayList<>();
        for (ContentSection contentSection : contentSectionList) {
            if (DataUtil.safeEqual(contentSection.getParentId(), dataNode.getKey())) {
                TreeDataDTO childNode = new TreeDataDTO();
                childNode.setTitle(contentSection.getName());
                childNode.setKey(contentSection.getId());
                childNode.setChildren(handleChildrenContent(childNode, contentSectionList));
                childNodeList.add(childNode);
            }
        }
        return childNodeList;
    }
}
