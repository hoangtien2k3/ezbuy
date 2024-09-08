/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.UploadImagesDTO;
import com.ezbuy.settingmodel.dto.request.*;
import com.ezbuy.settingmodel.dto.response.SearchImageResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
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
