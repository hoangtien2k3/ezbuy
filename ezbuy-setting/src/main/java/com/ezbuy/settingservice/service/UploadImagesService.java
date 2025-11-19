package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.UploadImagesDTO;
import com.ezbuy.settingmodel.dto.request.*;
import com.ezbuy.settingmodel.dto.response.SearchImageResponse;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface UploadImagesService {
    Mono<DataResponse<List<UploadImagesDTO>>> uploadFile(UploadImageRequest request);

    Mono<DataResponse<UploadImagesDTO>> createFolder(CreateFileRequest request);

    Mono<DataResponse<List<UploadImagesDTO>>> updateImages(UpdateImageRequest request);

    Mono<DataResponse<?>> deleteFolder(DeleteFolderRequest request);

    Mono<DataResponse<UploadImagesDTO>> renameFolder(RenameFolderRequest request);

    Mono<DataResponse<SearchImageResponse>> searchImages(SearchImageRequest request);

    Mono<DataResponse<UploadImagesDTO>> deleteImage(DeleteImageRequest request);

    Mono<DataResponse<UploadImagesDTO>> getInfo(String id);

    Mono<DataResponse<List<UploadImagesDTO>>> getAllFolder();
}
