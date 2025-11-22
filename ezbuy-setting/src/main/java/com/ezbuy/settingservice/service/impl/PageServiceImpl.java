package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingservice.model.dto.ContentDisplayDTO;
import com.ezbuy.settingservice.model.dto.PageDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.dto.request.ChangePageStatusRequest;
import com.ezbuy.settingservice.model.dto.request.ContentDisplayRequest;
import com.ezbuy.settingservice.model.dto.request.PageCreatingRequest;
import com.ezbuy.settingservice.model.dto.request.PagePolicyRequest;
import com.ezbuy.settingservice.model.dto.request.SearchingPageRequest;
import com.ezbuy.settingservice.model.entity.ContentDisplay;
import com.ezbuy.settingservice.model.entity.Page;
import com.ezbuy.settingservice.model.dto.response.SearchingPageResponse;
import com.ezbuy.settingservice.constant.SettingConstant;
import com.ezbuy.settingservice.repository.ContentDisplayRepository;
import com.ezbuy.settingservice.repository.OptionSetValueRepository;
import com.ezbuy.settingservice.repository.PageRepository;
import com.ezbuy.settingservice.repositoryTemplate.ContentDisplayRepositoryTemplate;
import com.ezbuy.settingservice.repositoryTemplate.PageRepositoryTemplate;
import com.ezbuy.settingservice.service.PageService;
import com.ezbuy.core.cache.LocalCache;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.factory.ModelMapperFactory;
import com.ezbuy.core.factory.ObjectMapperFactory;
import com.ezbuy.core.model.TokenUser;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.MinioUtils;
import com.ezbuy.core.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageServiceImpl extends BaseServiceHandler implements PageService {

    private final PageRepository pageRepository;
    private final PageRepositoryTemplate pageRepositoryTemplate;
    private final ContentDisplayRepository contentDisplayRepository;
    private final ContentDisplayRepositoryTemplate contentDisplayRepositoryTemplate;
    private final OptionSetValueRepository optionSetValueRepository;
    private final MinioUtils minioUtils;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    @LocalCache(durationInMinute = 30, maxRecord = 10000, autoCache = true)
    public Mono<DataResponse<PageDTO>> getPage(String code) {
        String safeTrim = DataUtil.safeTrim(code);
        if (DataUtil.isNullOrEmpty(safeTrim)) {
            return Mono.error(new BusinessException(ErrorCode.BAD_REQUEST, "params.invalid.code"));
        }
        return this.pageRepository.getPageByPageLink(code.trim()).collectList().flatMap(pages -> {
            if (DataUtil.isNullOrEmpty(pages)) {
                return Mono.just(new DataResponse<>("success", null));
            }
            return this.contentDisplayRepository
                    .getContentDisplayByPage(pages.getFirst().getId())
                    .collectList()
                    .flatMap(contentDisplays -> {
                        List<ContentDisplayDTO> contentDisplayDTOList = new ArrayList<>();
                        contentDisplays.forEach(contentDisplay -> {
                            if (contentDisplay.getParentId() == null) {
                                contentDisplayDTOList.add(contentDisplayDTO(contentDisplay, contentDisplays));
                            }
                        });
                        PageDTO pageDTO = PageDTO.builder()
                                .id(pages.getFirst().getId())
                                .title(pages.getFirst().getTitle())
                                .code(pages.getFirst().getCode())
                                .logoUrl(pages.getFirst().getLogoUrl())
                                .status(pages.getFirst().getStatus())
                                .createAt(pages.getFirst().getCreateAt())
                                .createBy(pages.getFirst().getCreateBy())
                                .updateBy(pages.getFirst().getUpdateBy())
                                .updateAt(pages.getFirst().getUpdateAt())
                                .contentDisplayList(contentDisplayDTOList)
                                .title(pages.getFirst().getTitle())
                                .build();
                        return Mono.just(new DataResponse<>("success", pageDTO));
                    });
        });
    }

    @Override
    public Mono<SearchingPageResponse> searchPages(SearchingPageRequest request) {
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);

        Flux<Page> pages = pageRepositoryTemplate.queryPages(request);
        Mono<Long> countMono = pageRepositoryTemplate.countPages(request);
        return Mono.zip(pages.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchingPageResponse response = new SearchingPageResponse();
            response.setPages(zip.getT1());
            response.setPagination(pagination);

            return response;
        });
    }

    @Override
    public Mono<PageDTO> getDetailPage(String pageId) {
        Mono<Page> pageMono = pageRepository.findById(pageId);
        Mono<PageDTO> pageDTOMono = pageMono.map(p -> ModelMapperFactory.getInstance().map(p, PageDTO.class))
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "page.not.found")));

        Mono<List<ContentDisplayDTO>> listMono = contentDisplayRepositoryTemplate.getAllByPageId(pageId);
        return Mono.zip(pageDTOMono, listMono).map(zip -> {
            PageDTO pageDTO = zip.getT1();
            pageDTO.setContentDisplayList(zip.getT2());
            return pageDTO;
        });
    }

    @Override
    @Transactional
    public Mono<DataResponse<Page>> createPage(PageCreatingRequest request) {
        if (!DataUtil.isNullOrEmpty(request.getComponents())) {
            validateImagePageRequest(request.getComponents());
        }
        LocalDateTime now = LocalDateTime.now();
        return SecurityUtils.getCurrentUser()
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "user.null")))
                .flatMap(tokenUser -> {
                    String pageId = UUID.randomUUID().toString();
                    String code = DataUtil.safeTrim(request.getCode());
                    Page page = Page.builder()
                            .id(pageId)
                            .code(code)
                            .title(DataUtil.safeTrim(request.getTitle()))
                            .status(1)
                            .createAt(now)
                            .createBy(tokenUser.getUsername())
                            .updateAt(now)
                            .updateBy(tokenUser.getUsername())
                            .insert(true)
                            .build();
                    return validateRequest(null, request)
                            .then(pageRepository.save(page))
                            .switchIfEmpty(Mono.error(
                                    new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "page.insert.failed")))
                            .flatMap(p ->
                                    saveContentDisplay(request.getComponents(), tokenUser.getUsername(), now, pageId))
                            .thenReturn(new DataResponse<>("cuccess", page));
                });
    }

    private Mono<Void> validateDuplicateTitle(String title, String id) {
        return pageRepository
                .findByTitle(DataUtil.safeTrim(title))
                .doOnNext(page -> System.out.println("duplicate title " + page))
                .any(page -> !Objects.equals(page.getId(), id))
                .flatMap(aBoolean -> {
                    if (aBoolean) {
                        return Mono.error(new BusinessException(ErrorCode.BAD_REQUEST, "page.title.duplicate"));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> validateDuplicateCode(String code, String id) {
        return pageRepository
                .findByCode(DataUtil.safeTrim(code))
                .any(page -> !Objects.equals(page.getId(), id))
                .flatMap(match -> {
                    if (match) {
                        return Mono.error(new BusinessException(ErrorCode.BAD_REQUEST, "page.code.duplicate"));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> validateRequest(String pageId, PageCreatingRequest request) {
        return validateDuplicateTitle(request.getTitle(), pageId)
                .then(validateDuplicateCode(request.getCode(), pageId))
                .then();
    }

    private Mono<Boolean> uploadResourceToMinio(List<ContentDisplayRequest> components) {
        if (!DataUtil.isNullOrEmpty(components)) {
            return Flux.fromIterable(components)
                    .flatMap(content -> {
                        if (Objects.equals(content.getIsUploadImage(), Boolean.TRUE)) {
                            byte[] file = Base64.decodeBase64(content.getImageBase64());
                            minioUtils.uploadFile(content.getImage(), file, bucket);
                        }
                        if (Objects.equals(content.getIsUploadBackground(), Boolean.TRUE)) {
                            byte[] file = Base64.decodeBase64(content.getBackgroundBase64());
                            minioUtils.uploadFile(content.getBackgroundImage(), file, bucket);
                        }
                        if (Objects.equals(content.getIsUploadIcon(), Boolean.TRUE)) {
                            byte[] file = Base64.decodeBase64(content.getIconBase64());
                            minioUtils.uploadFile(content.getIcon(), file, bucket);
                        }
                        uploadResourceToMinio(content.getContentDisplayDTOList());
                        return Mono.just(Boolean.TRUE);
                    })
                    .collectList()
                    .map(result -> Boolean.TRUE);
        } else {
            return Mono.just(Boolean.TRUE);
        }
    }

    private void validateImagePageRequest(List<ContentDisplayRequest> request) {
        if (!DataUtil.isNullOrEmpty(request)) {
            for (ContentDisplayRequest content : request) {
                if (!DataUtil.isNullOrEmpty(content.getImage()) && !DataUtil.isNullOrEmpty(content.getImageBase64())) {
                    if (!Base64.isBase64(content.getImageBase64())) {
                        throw new BusinessException(ErrorCode.INVALID_PARAMS, "");
                    }
                    String filePath = SettingConstant.MINIO_FOLDER.BACKGROUND_FOLDER + SettingConstant.FILE_SEPARATOR
                            + UUID.randomUUID() + "-" + FilenameUtils.getName(content.getImage());
                    content.setImage(filePath);
                    content.setIsUploadImage(true);
                }

                if (!DataUtil.isNullOrEmpty(content.getBackgroundImage())
                        && !DataUtil.isNullOrEmpty(content.getBackgroundBase64())) {
                    if (!Base64.isBase64(content.getBackgroundBase64())) {
                        throw new BusinessException(ErrorCode.INVALID_PARAMS, "");
                    }
                    String filePath = SettingConstant.MINIO_FOLDER.BACKGROUND_FOLDER + SettingConstant.FILE_SEPARATOR
                            + UUID.randomUUID() + "-" + FilenameUtils.getName(content.getBackgroundImage());
                    content.setBackgroundImage(filePath);
                    content.setIsUploadBackground(true);
                }
                if (!DataUtil.isNullOrEmpty(content.getIcon()) && !DataUtil.isNullOrEmpty(content.getIconBase64())) {
                    if (!Base64.isBase64(content.getIconBase64())) {
                        throw new BusinessException(ErrorCode.INVALID_PARAMS, "");
                    }
                    String filePath = SettingConstant.MINIO_FOLDER.ICON_FOLDER + SettingConstant.FILE_SEPARATOR
                            + UUID.randomUUID() + "-" + FilenameUtils.getName(content.getIcon());
                    content.setIcon(filePath);
                    content.setIsUploadIcon(true);
                }
                if (!DataUtil.isNullOrEmpty(content.getContentDisplayDTOList())) {
                    validateImagePageRequest(content.getContentDisplayDTOList());
                }
            }
        }
    }

    @Override
    @Transactional
    public Mono<DataResponse<Page>> editPage(PageCreatingRequest request) {
        if (DataUtil.isNullOrEmpty(request.getId())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "page.id.not.empty");
        }
        if (!DataUtil.isNullOrEmpty(request.getComponents())) {
            validateImagePageRequest(request.getComponents());
        }

        LocalDateTime now = LocalDateTime.now();
        return SecurityUtils.getCurrentUser()
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "user.null")))
                .flatMap(tokenUser -> pageRepository
                        .findById(request.getId())
                        .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "page.not.found")))
                        .flatMap(page -> validateRequest(page.getId(), request).thenReturn(page))
                        .flatMap(page ->
                                deleteOldDetails(page, tokenUser.getId()).thenReturn(page))
                        .doOnNext(page -> {
                            page.setCode(DataUtil.safeTrim(request.getCode()));
                            page.setTitle(DataUtil.safeTrim(request.getTitle()));
                            page.setUpdateAt(now);
                            page.setUpdateBy(tokenUser.getUsername());
                        })
                        .flatMap(pageRepository::save)
                        .flatMap(page ->
                                saveContentDisplay(request.getComponents(), tokenUser.getUsername(), now, page.getId()))
                        .thenReturn(new DataResponse<>("cuccess", null)));
    }

    private Mono<Boolean> deleteOldDetails(Page page, String userId) {
        return contentDisplayRepository
                .getContentDisplayByPage(page.getId())
                .collectList()
                .flatMapMany(contentDisplayRepository::deleteAll)
                .then(Mono.just(Boolean.TRUE));
    }

    private Mono<List<ContentDisplay>> saveContentDisplay(
            List<ContentDisplayRequest> cd, String username, LocalDateTime now, String pageId) {
        List<ContentDisplay> contentDisplays = initContentDisplay(cd, username, now, null, pageId);
        if (!DataUtil.isNullOrEmpty(contentDisplays)) {
            return uploadResources(contentDisplays)
                    .thenMany(contentDisplayRepository.saveAll(contentDisplays))
                    .collectList()
                    .map(res -> res.stream()
                            .filter(p -> Objects.isNull(p.getParentId()))
                            .collect(Collectors.toList()));
        }
        return Mono.empty();
    }

    private Mono<Boolean> uploadResources(List<ContentDisplay> contentDisplays) {
        return Flux.fromIterable(contentDisplays)
                .doOnNext(contentDisplay -> log.info("upload minio for content display {}", contentDisplay.getId()))
                .flatMap(this::uploadResource)
                .collectList()
                .thenReturn(true);
    }

    private Mono<Boolean> uploadResource(ContentDisplay contentDisplay) {
        List<Mono<?>> publishers = new LinkedList<>();
        contentDisplay.setIcon(getNormalizeBase64(contentDisplay.getIcon()));
        contentDisplay.setImage(getNormalizeBase64(contentDisplay.getImage()));
        contentDisplay.setBackgroundImage(getNormalizeBase64(contentDisplay.getBackgroundImage()));
        // upload icon
        if (!DataUtil.isNullOrEmpty(contentDisplay.getIcon())) {
            if (Base64.isBase64(contentDisplay.getIcon())) {
                var iconPublisher = minioUtils
                        .uploadFile(
                                Base64.decodeBase64(contentDisplay.getIcon()),
                                minioUtils.getMinioProperties().getBucket(),
                                UUID.randomUUID().toString())
                        .map(minioUtils::getObjectUrl)
                        .doOnNext(contentDisplay::setIcon);
                publishers.add(iconPublisher);
            }
        }

        // upload image
        if (!DataUtil.isNullOrEmpty(contentDisplay.getImage())) {
            if (Base64.isBase64(contentDisplay.getImage())) {
                var imagePublisher = minioUtils
                        .uploadFile(
                                Base64.decodeBase64(contentDisplay.getImage()),
                                minioUtils.getMinioProperties().getBucket(),
                                UUID.randomUUID().toString())
                        .map(minioUtils::getObjectUrl)
                        .doOnNext(contentDisplay::setImage);
                publishers.add(imagePublisher);
            }
        }

        // upload background
        if (!DataUtil.isNullOrEmpty(contentDisplay.getBackgroundImage())) {
            if (Base64.isBase64(contentDisplay.getBackgroundImage())) {
                var bgPublisher = minioUtils
                        .uploadFile(
                                Base64.decodeBase64(contentDisplay.getBackgroundImage()),
                                minioUtils.getMinioProperties().getBucket(),
                                UUID.randomUUID().toString())
                        .map(minioUtils::getObjectUrl)
                        .doOnNext(contentDisplay::setBackgroundImage);
                publishers.add(bgPublisher);
            }
        }

        return Mono.zip(publishers, result -> result).thenReturn(true);
    }

    private String getNormalizeBase64(String input) {
        final String BASE64_PREFIX = "data:";
        if (!DataUtil.isNullOrEmpty(input) && input.startsWith(BASE64_PREFIX)) {
            return input.split(",")[1];
        }
        return input;
    }

    private List<ContentDisplay> initContentDisplay(
            List<ContentDisplayRequest> cd, String userId, LocalDateTime now, String parentId, String pageId) {
        List<ContentDisplay> contentDisplays = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(cd)) {
            for (ContentDisplayRequest content : cd) {
                ContentDisplay contentDisplay =
                        ObjectMapperFactory.getInstance().convertValue(content, ContentDisplay.class);
                String id = UUID.randomUUID().toString();
                contentDisplay.setId(id);
                contentDisplay.setCreateAt(now);
                contentDisplay.setUpdateAt(now);
                contentDisplay.setCreateBy(userId);
                contentDisplay.setUpdateBy(userId);
                contentDisplay.setParentId(parentId);
                contentDisplay.setPageId(pageId);
                contentDisplay.setStatus(1);
                contentDisplay.setIsOriginal(false);
                contentDisplay.setInsert(true);
                contentDisplays.add(contentDisplay);
                contentDisplays.addAll(initContentDisplay(content.getContentDisplayDTOList(), userId, now, id, pageId));
            }
        }
        return contentDisplays;
    }

    public ContentDisplayDTO contentDisplayDTO(ContentDisplay display, List<ContentDisplay> contentDisplays) {
        ContentDisplayDTO contentDisplayDTO = ContentDisplayDTO.builder()
                .id(display.getId())
                .pageId(display.getPageId())
                .type(display.getType())
                .content(display.getContent())
                .telecomServiceId(display.getTelecomServiceId())
                .title(display.getTitle())
                .subtitle(display.getSubtitle())
                .icon(display.getIcon())
                .displayOrder(display.getDisplayOrder())
                .image(display.getImage())
                .backgroundImage(display.getBackgroundImage())
                .refUrl(display.getRefUrl())
                .parentId(display.getParentId())
                .status(display.getStatus())
                .createAt(display.getCreateAt())
                .createBy(display.getCreateBy())
                .updateAt(display.getUpdateAt())
                .updateBy(display.getUpdateBy())
                .contentDisplayDTOList(new ArrayList<>())
                .build();
        contentDisplays.forEach(contentDisplay -> {
            if (Objects.equals(display.getId(), contentDisplay.getParentId())) {
                ContentDisplayDTO dto = contentDisplayDTO(contentDisplay, contentDisplays);
                contentDisplayDTO.getContentDisplayDTOList().add(dto);
            }
        });
        return contentDisplayDTO;
    }

    @Override
    public Mono<DataResponse<String>> policyPage(PagePolicyRequest request) {
        if (DataUtil.isNullOrEmpty(request.getPolicyCode()) || DataUtil.isNullOrEmpty(request.getPolicyCode())) {
            return Mono.just(new DataResponse<>(null, "cuccess", null));
        }
        return optionSetValueRepository
                .findByOptionSetCodeAndOptionValueCode(
                        SettingConstant.OptionSetCode.POLICY_CODE,
                        request.getPolicyCode() + "_" + request.getServiceId())
                .flatMap(optionSetValue -> {
                    String policyUrl = null;
                    if (optionSetValue != null) {
                        policyUrl = optionSetValue.getValue();
                    }
                    return Mono.just(new DataResponse<>(null, "success", policyUrl));
                });
    }

    @Override
    public Mono<DataResponse<PageDTO>> changeStatus(ChangePageStatusRequest request) {
        String pageId = DataUtil.safeTrim(request.getPageId());
        Integer status = request.getStatus();
        return Mono.zip(SecurityUtils.getCurrentUser().switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "user.null"))),
                        pageRepository.findById(pageId)
                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "page.not.found"))))
                .flatMap(zip -> {
                    Page page = zip.getT2();
                    TokenUser tokenUser = zip.getT1();
                    if (page.getStatus().equals(status)) {
                        if (status.equals(SettingConstant.PAGE_STATUS.LOCK)) {
                            return Mono.error(new BusinessException(ErrorCode.BAD_REQUEST, "page.status.locked"));
                        } else {
                            return Mono.error(
                                    new BusinessException(ErrorCode.BAD_REQUEST, "page.status.unlocked"));
                        }
                    }

                    page.setUpdateBy(tokenUser.getUsername());
                    page.setUpdateAt(LocalDateTime.now());
                    page.setStatus(status);
                    return pageRepository.save(page);
                })
                .map(page -> {
                    PageDTO dto = new PageDTO();
                    BeanUtils.copyProperties(page, dto);
                    return new DataResponse<>("success", dto);
                });
    }
}
