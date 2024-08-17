package com.ezbuy.settingservice.service.impl;

import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.constants.MessageConstant;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.model.TokenUser;
import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.framework.utils.MinioUtils;
import com.ezbuy.framework.utils.SecurityUtils;
import com.ezbuy.framework.utils.Translator;
import com.ezbuy.settingmodel.dto.request.*;
import com.ezbuy.settingmodel.constants.Constants;
import com.ezbuy.settingmodel.dto.FileDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.dto.UploadImagesDTO;
import com.ezbuy.settingmodel.dto.response.SearchImageResponse;
import com.ezbuy.settingmodel.model.UploadImages;
import com.ezbuy.settingservice.repository.UploadImagesRepository;
import com.ezbuy.settingservice.repositoryTemplate.UploadImageRepositoryTemplate;
import com.ezbuy.settingservice.service.UploadImagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.activation.MimetypesFileTypeMap;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UploadImagesImpl implements UploadImagesService {

    //    private final FileService fileService;
    private final MinioUtils minioUtils;
    private final UploadImagesRepository uploadImagesRepository;
    private final UploadImageRepositoryTemplate uploadImageRepositoryTemplate;

    @Override
    public Mono<DataResponse<List<UploadImagesDTO>>> uploadFile(UploadImageRequest request) {
        //validate du lieu dau vao
        if (DataUtil.isNullOrEmpty(request.getImages())) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "params.invalid.code"));
        }

        Set<String> counter = new HashSet<>();
        if (!DataUtil.isNullOrEmpty(request.getImages())) {
            request.getImages().forEach(fileDTO -> {
                if (counter.contains(fileDTO.getName())) {
                    throw new BusinessException(CommonErrorCode.BAD_REQUEST, Translator.toLocaleVi("upload.image.existed", fileDTO.getName()));
                }
                counter.add(fileDTO.getName());
            });
        }

        Mono<UploadImages> parentInfoMono;
        if (DataUtil.isNullOrEmpty(request.getParentId())) {
            parentInfoMono = Mono.just(new UploadImages());
        } else {
            Throwable error = new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.root.folder.notfound");
            parentInfoMono = uploadImagesRepository
                    .findById(request.getParentId())
                    .switchIfEmpty(Mono.error(error));
        }

        return Mono.zip(SecurityUtils.getCurrentUser(), parentInfoMono).flatMapMany(zip -> {
            TokenUser tokenUser = zip.getT1();
            UploadImages parent = zip.getT2();

            parent.setUpdateAt(LocalDateTime.now());
            parent.setUpdateBy(tokenUser.getUsername());
            uploadImagesRepository.save(parent).subscribe();

            return Flux.fromIterable(request.getImages())
                    .flatMap(fileDTO -> {
                        if (DataUtil.isNullOrEmpty(fileDTO.getId())) {
                            return createImage(fileDTO, tokenUser, parent);
                        }
                        return updateImage(fileDTO, tokenUser, parent);
                    }).map(this::mapToDto);
        }).collectList().map(res -> new DataResponse<>("success", res));
    }

    /**
     * save new image
     *
     * @param fileDTO   request payload
     * @param tokenUser user login
     * @param parent    parent folder
     * @return saved image
     */
    private Mono<UploadImages> createImage(FileDTO fileDTO, TokenUser tokenUser, UploadImages parent) {
        byte[] file = Base64.decodeBase64(fileDTO.getImageBase64());
        validateFileSize(file);
        String filePath = DataUtil.safeToString(parent.getPath()) + fileDTO.getName();
        return validateDuplicateName(parent.getId(), DataUtil.safeTrim(fileDTO.getName()), null)
                .then(minioUtils.uploadFile(file, minioUtils.getMinioProperties().getBucket(), filePath))
                .flatMap(objectPath -> Mono.zip(Mono.just(objectPath), Mono.just(fileDTO.getName())))
                .flatMap(urlAndName -> {
                    UploadImages uploadImages = new UploadImages();
                    uploadImages.setId(UUID.randomUUID().toString());
                    uploadImages.setName(urlAndName.getT2());
                    uploadImages.setPath(urlAndName.getT1());
                    uploadImages.setType(Constants.UPLOAD_TYPE.FILE);
                    uploadImages.setParentId(DataUtil.safeToString(parent.getId(), null));
                    uploadImages.setStatus(Constants.UPLOAD_STATUS.ACTIVE);
                    uploadImages.setCreateAt(LocalDateTime.now());
                    uploadImages.setCreateBy(tokenUser.getUsername());
                    uploadImages.setUpdateAt(LocalDateTime.now());
                    uploadImages.setNew(true);
                    return uploadImagesRepository.save(uploadImages);
                });
    }

    private Mono<Boolean> validateDuplicateName(String parentId, String fileName, String ignoreId) {
        return uploadImagesRepository.findByParentIdAndName(parentId, fileName)
                .filter(element -> !Objects.equals(element.getId(), ignoreId))
                .collectList()
                .flatMap(sameName -> {
                    if (!sameName.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, Translator.toLocaleVi("upload.image.existed", fileName)));
                    }
                    return Mono.just(true);
                });
    }

    private void validateFileSize(byte[] bytes) {
        int fileSizeMB = bytes.length / (1024 * 1024);
        if (fileSizeMB >= 10) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.size.limit");
        }
    }

    private Mono<UploadImages> updateImage(FileDTO fileDTO, TokenUser tokenUser, UploadImages parent) {
        byte[] file = Base64.decodeBase64(fileDTO.getImageBase64());
        validateFileSize(file);
        return uploadImagesRepository.findById(fileDTO.getId())
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.image.notfound")))
                .flatMap(oldInfo -> validateDuplicateName(
                        oldInfo.getParentId(),
                        DataUtil.safeTrim(fileDTO.getName()),
                        oldInfo.getId()).thenReturn(oldInfo)
                )
                .flatMap(oldInfo -> {
                    String newPath = parent.getPath() + DataUtil.safeTrim(fileDTO.getName());
                    return minioUtils.uploadFile(file, minioUtils.getMinioProperties().getBucket(), newPath)
                            .doOnNext((uploadResult) -> {
                                        if (!oldInfo.getPath().equals(newPath)) {
                                            minioUtils.removeObject(
                                                    minioUtils.getMinioProperties().getBucket(), oldInfo.getPath()).subscribe();
                                        }
                                    }
                            )
                            .flatMap(uploadResult -> {
                                oldInfo.setName(fileDTO.getName());
                                oldInfo.setPath(newPath);
                                oldInfo.setUpdateAt(LocalDateTime.now());
                                oldInfo.setUpdateBy(tokenUser.getUsername());
                                oldInfo.setNew(false);
                                return uploadImagesRepository.save(oldInfo);
                            });
                });
    }

    @Override
    public Mono<DataResponse<UploadImagesDTO>> createFolder(CreateFileRequest request) {
        if (!request.getType().equals(Constants.UPLOAD_TYPE.FOLDER)) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.not.folder"));
        }

        Mono<DataResponse<UploadImagesDTO>> createFolder = Mono.zip(
                SecurityUtils.getCurrentUser(),
                DataUtil.isNullOrEmpty(request.getParentId()) ?
                        Mono.just(new UploadImages()) :
                        uploadImagesRepository.findById(request.getParentId()).switchIfEmpty(
                                Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.root.folder.notfound"))
                        )
        ).flatMap(zip -> {
            TokenUser tokenUser = zip.getT1();
            UploadImages parent = zip.getT2();

            String path = request.getName() + "/";
            String parentPath = DataUtil.safeToString(parent.getPath(), "");
            if (!DataUtil.isNullOrEmpty(parentPath)) {
                path = parentPath + path;
            }

            UploadImages uploadImages = new UploadImages();
            uploadImages.setId(UUID.randomUUID().toString());
            uploadImages.setName(request.getName());
            uploadImages.setPath(path);
            uploadImages.setType(Constants.UPLOAD_TYPE.FOLDER);
            uploadImages.setParentId(request.getParentId());
            uploadImages.setStatus(Constants.UPLOAD_STATUS.ACTIVE);
            uploadImages.setCreateAt(LocalDateTime.now());
            uploadImages.setUpdateAt(LocalDateTime.now());
            uploadImages.setCreateBy(tokenUser.getUsername());
            uploadImages.setNew(true);
            return uploadImagesRepository.save(uploadImages);
        }).flatMap(uploadImages -> minioUtils.makeFolder(minioUtils.getMinioProperties().getBucket(), uploadImages.getPath()).thenReturn(uploadImages)
        ).map(result -> {
            UploadImagesDTO dto = mapToDto(result);
            return new DataResponse<>("success", dto);
        });

        String folderName = DataUtil.safeToString(request.getName()).trim();
        return uploadImagesRepository.findByName(folderName.toLowerCase())
                .collectList()
                .flatMap(listByName -> {
                    if (!listByName.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.folder.existed"));
                    }
                    return createFolder;
                });
    }

    @Override
    public Mono<DataResponse<List<UploadImagesDTO>>> updateImages(UpdateImageRequest request) {
        return Flux.fromIterable(request.getImages())
                .flatMap(this::updateImage)
                .collectList()
                .map(uploadDTOS -> new DataResponse<>("success", uploadDTOS));
    }

    @Override
    public Mono<DataResponse<?>> deleteFolder(DeleteFolderRequest request) {
        Mono<UploadImages> infoDeleteFolderMono = uploadImagesRepository.findById(request.getId())
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.folder.notfound"))
                )
                .flatMap(uploadImg -> {
                    uploadImg.setStatus(Constants.UPLOAD_STATUS.INACTIVE);
                    return uploadImagesRepository.save(uploadImg);
                });
        Flux<UploadImages> infoChildrenFlux = uploadImagesRepository.findAllByParentId(request.getId());
        return Mono.zip(
                infoDeleteFolderMono,
                infoChildrenFlux.collectList(),
                SecurityUtils.getCurrentUser()
        ).flatMap(zip -> {
            UploadImages deleteFolder = zip.getT1();
            List<UploadImages> children = zip.getT2();
            log.info("children images {}", children);
            TokenUser tokenUser = zip.getT3();
            if (request.getDeleteType().equals(1)) {
                for (UploadImages child : children) {
                    child.setStatus(Constants.UPLOAD_STATUS.INACTIVE);
                    child.setUpdateBy(tokenUser.getUsername());
                    child.setUpdateAt(LocalDateTime.now());
                }
                return Flux.fromIterable(children).flatMap(image -> Mono.zip(
                        uploadImagesRepository.save(image),
                        minioUtils.removeObject(minioUtils.getMinioProperties().getBucket(), image.getPath())
                )).collectList();
            }
            return getPathByObjectId(deleteFolder.getParentId())
                    .flatMap(parentPath -> Flux.fromIterable(children).flatMap(child -> {
                        String oldPath = child.getPath();
                        String newPath = parentPath + UUID.randomUUID() + "_" + child.getName();

                        child.setParentId(null);
                        child.setPath(newPath);
                        child.setUpdateBy(tokenUser.getUsername());
                        child.setUpdateAt(LocalDateTime.now());
                        return Mono.zip(
                                minioUtils.copyObject(minioUtils.getMinioProperties().getBucket(), oldPath, newPath),
                                uploadImagesRepository.save(child)
                        );
                    }).collectList());
        }).thenReturn(new DataResponse<>("success", null));
    }

    @Override
    public Mono<DataResponse<UploadImagesDTO>> renameFolder(RenameFolderRequest request) {
        Mono<UploadImages> findFolder = uploadImagesRepository.findById(request.getId().trim())
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.folder.notfound")));

        String folderName = DataUtil.safeTrim(request.getNewName());
        Flux<UploadImages> findSameName = uploadImagesRepository.findByName(folderName);

        return findFolder.flatMap(folder -> findSameName.collectList()
                .flatMap(itemSameName -> {
                    for (UploadImages element : itemSameName) {
                        if (!Objects.equals(element.getId(), folder.getId())) {
                            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.folder.existed"));
                        }
                    }
                    return Mono.zip(
                            SecurityUtils.getCurrentUser(),
                            Mono.just(folder),
                            getPathByObjectId(folder.getParentId())
                                    .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, ""))));
                })
        ).flatMap(zip -> {
            TokenUser tokenUser = zip.getT1();
            UploadImages uploadImages = zip.getT2();
            String parentPath = zip.getT3();

            if (uploadImages.getName().equals(request.getNewName().trim())) {
                return Mono.just(
                        new DataResponse<>("success", mapToDto(uploadImages))
                );
            }

            String oldPath = uploadImages.getPath() + "/";
            String newPath = parentPath + request.getNewName() + "/";

            uploadImages.setName(request.getNewName());
            uploadImages.setUpdateAt(LocalDateTime.now());
            uploadImages.setPath(newPath);
            uploadImages.setUpdateBy(tokenUser.getUsername());
            uploadImages.setNew(false);

            return minioUtils.copyObject(minioUtils.getMinioProperties().getBucket(), oldPath, newPath)
                    .doOnNext((unused) -> minioUtils.removeObject(minioUtils.getMinioProperties().getBucket(), oldPath).subscribe())
                    .then(uploadImagesRepository.save(uploadImages))
                    .flatMap(saved -> {
                        UploadImagesDTO dto = mapToDto(saved);
                        return updatePathFolderItem(saved).thenReturn(
                                new DataResponse<>("success", dto)
                        );
                    });
        });
    }

    /**
     * update path for folder items
     *
     * @param folder folder info
     */
    private Mono<Void> updatePathFolderItem(UploadImages folder) {
        return uploadImagesRepository.findAllByParentId(folder.getId())
                .flatMap(fileItem -> {
                    String oldPath = fileItem.getPath();
                    String newPath = folder.getPath() + fileItem.getName();
                    fileItem.setPath(newPath);
                    return minioUtils.copyObject(minioUtils.getMinioProperties().getBucket(), oldPath, newPath)
                            .then(uploadImagesRepository.save(fileItem));
                })
                .collectList().then();
    }

    private Mono<String> getPathByObjectId(String id) {
        if (DataUtil.isNullOrEmpty(id)) {
            return Mono.just("");
        }
        return uploadImagesRepository.findById(id)
                .map(uploadImages -> DataUtil.safeToString(uploadImages.getPath()));
    }

    /**
     * update existed image
     *
     * @param uploadDTO update image
     * @return updated image
     */
    private Mono<UploadImagesDTO> updateImage(UploadDTO uploadDTO) {
        return uploadImagesRepository.findById(uploadDTO.getId().trim())
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.image.notfound")))
                .flatMap(imageInfo -> {
                    Mono<String> parentPathMono;
                    if (DataUtil.isNullOrEmpty(imageInfo.getParentId())) {
                        parentPathMono = Mono.just("");
                    } else {
                        parentPathMono = uploadImagesRepository.findById(imageInfo.getParentId())
                                .flatMap(parent -> {
                                    parent.setUpdateAt(LocalDateTime.now());
                                    return uploadImagesRepository.save(parent);
                                })
                                .map(UploadImages::getPath);
                    }
                    return Mono.zip(Mono.just(imageInfo), parentPathMono, SecurityUtils.getCurrentUser());
                })
                .flatMap(zip -> {
                    UploadImages uploadImg = zip.getT1();
                    String parentPath = zip.getT2();
                    TokenUser tokenUser = zip.getT3();

                    byte[] bytes = Base64.decodeBase64(uploadDTO.getImage());
                    validateFileSize(bytes);
                    String newName = uploadDTO.getName();
                    String oldPath = uploadImg.getPath();
                    String newPath = parentPath + newName;

                    uploadImg.setPath(newPath);
                    uploadImg.setName(newName);
                    uploadImg.setUpdateAt(LocalDateTime.now());
                    uploadImg.setUpdateBy(tokenUser.getUsername());
                    return validateDuplicateName(uploadImg.getParentId(), newName, uploadImg.getId())
                            .doOnNext(bool -> minioUtils.removeObject(minioUtils.getMinioProperties().getBucket(), oldPath).subscribe())
                            .then(minioUtils.uploadFile(bytes, minioUtils.getMinioProperties().getBucket(), newPath))
                            .then(uploadImagesRepository.save(uploadImg));
                })
                .map(this::mapToDto);
    }

    @Override
    public Mono<DataResponse<SearchImageResponse>> searchImages(SearchImageRequest request) {
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
        return Mono.zip(
                uploadImageRepositoryTemplate.queryList(request).collectList(),
                uploadImageRepositoryTemplate.count(request)
        ).flatMap(zip -> {
            List<UploadImagesDTO> content = zip.getT1();
            content.forEach(this::updatePathToResponse);
            Long totalRecords = zip.getT2();

            SearchImageResponse response = SearchImageResponse.builder()
                    .content(content)
                    .pagination(PaginationDTO.builder()
                            .pageIndex(request.getPageIndex())
                            .pageSize(request.getPageSize())
                            .totalRecords(totalRecords).build())
                    .build();
            return Mono.just(response);
        }).map(res -> new DataResponse<>("success", res));
    }

    @Override
    public Mono<DataResponse<UploadImagesDTO>> deleteImage(DeleteImageRequest request) {
        return uploadImagesRepository.findById(DataUtil.safeTrim(request.getId()))
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.image.notfound")))
                .doOnNext(uploadImg -> minioUtils.removeObject(minioUtils.getMinioProperties().getBucket(), uploadImg.getPath()).subscribe())
                .flatMap(uploadImages -> {
                    uploadImages.setStatus(Constants.UPLOAD_STATUS.INACTIVE);
                    return uploadImagesRepository.save(uploadImages);
                })
                .map(this::mapToDto)
                .map(dto -> new DataResponse<>("success", dto));
    }

    @Override
    public Mono<DataResponse<UploadImagesDTO>> getInfo(String id) {
        return uploadImagesRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.folder.notfound")))
                .flatMap(uploadImg -> {
                    UploadImagesDTO dto = mapToDto(uploadImg);
                    return uploadImagesRepository.findAllByParentId(uploadImg.getId())
                            .map(this::mapToDto)
                            .collectList()
                            .doOnNext(dto::setChildren)
                            .thenReturn(dto);
                })
                .map(dto -> new DataResponse<>("success", dto));
    }

    @Override
    public Mono<DataResponse<List<UploadImagesDTO>>> getAllFolder() {
        return uploadImagesRepository.findAllFolder()
                .map(this::mapToDto)
                .collectList()
                .map(folders -> new DataResponse<>("success", folders));
    }

    private UploadImagesDTO mapToDto(UploadImages uploadImage) {
        UploadImagesDTO dto = new UploadImagesDTO();
        BeanUtils.copyProperties(uploadImage, dto);
        if (uploadImage.getType().equals(1)) {
            String path = appendFullPath(uploadImage.getPath());
            dto.setPath(path);
        }
        return dto;
    }

    public void updatePathToResponse(UploadImagesDTO dto) {
        if (dto.getType().equals(1)) {
            String newPath = appendFullPath(dto.getPath());
            dto.setPath(newPath);
        }
        if (!DataUtil.isNullOrEmpty(dto.getPreviewImages())) {
            List<String> previewFullPaths = dto.getPreviewImages().stream().map(this::appendFullPath)
                    .collect(Collectors.toList());
            dto.setPreviewImages(previewFullPaths);
        }
    }

    public String appendFullPath(String rawPath) {
        return minioUtils.getMinioProperties().getPublicUrl() + "/"
                + minioUtils.getMinioProperties().getBucket() + "/"
                + rawPath;
    }

    private String getContentType(String fileName) {
        MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
        return typeMap.getContentType(fileName);
    }
}
