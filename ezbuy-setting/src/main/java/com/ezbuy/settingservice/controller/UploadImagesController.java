package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.UploadImagesDTO;
import com.ezbuy.settingmodel.dto.request.*;
import com.ezbuy.settingmodel.dto.response.SearchImageResponse;
import com.ezbuy.settingservice.service.UploadImagesService;
import com.ezbuy.core.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = UrlPaths.Upload.PREFIX)
public class UploadImagesController {
    private final UploadImagesService uploadImagesService;

    @PostMapping(UrlPaths.Upload.FILE_UPLOAD)
    public Mono<DataResponse<List<UploadImagesDTO>>> uploadFile(@RequestBody UploadImageRequest param) {
        return uploadImagesService.uploadFile(param).subscribeOn(Schedulers.boundedElastic());
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping(UrlPaths.Upload.CREATE_FOLDER)
    public Mono<DataResponse<UploadImagesDTO>> createFolder(@Valid @RequestBody CreateFileRequest request) {
        return uploadImagesService.createFolder(request);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping(UrlPaths.Upload.UPDATE_IMAGES)
    public Mono<DataResponse<List<UploadImagesDTO>>> updateImages(@Valid @RequestBody UpdateImageRequest request) {
        return uploadImagesService.updateImages(request);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping(UrlPaths.Upload.DELETE_FOLDER)
    public Mono<DataResponse<?>> deleteFolder(@Valid @RequestBody DeleteFolderRequest request) {
        return uploadImagesService.deleteFolder(request);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping(UrlPaths.Upload.RENAME_FOLDER)
    public Mono<DataResponse<UploadImagesDTO>> renameFolder(@Valid @RequestBody RenameFolderRequest request) {
        return uploadImagesService.renameFolder(request);
    }

    @PreAuthorize("hasAnyAuthority('user', 'admin', 'system')")
    @GetMapping(UrlPaths.Upload.SEARCH)
    public Mono<DataResponse<SearchImageResponse>> search(SearchImageRequest request) {
        return uploadImagesService.searchImages(request);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping(UrlPaths.Upload.DELETE_FILE)
    public Mono<DataResponse<UploadImagesDTO>> deleteImage(@Valid @RequestBody DeleteImageRequest request) {
        return uploadImagesService.deleteImage(request);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping(UrlPaths.Upload.INFO)
    public Mono<DataResponse<UploadImagesDTO>> getInfo(@PathVariable("id") String id) {
        return uploadImagesService.getInfo(id);
    }

    @PreAuthorize("hasAnyAuthority('user', 'admin', 'system')")
    @GetMapping(UrlPaths.Upload.FOLDER)
    public Mono<DataResponse<List<UploadImagesDTO>>> getAllFolder() {
        return uploadImagesService.getAllFolder();
    }
}
