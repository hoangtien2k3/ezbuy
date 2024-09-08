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
package com.ezbuy.settingservice.utils;

import com.ezbuy.settingservice.constant.SettingConstant;
import io.hoangtien2k3.commons.utils.MinioUtils;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FileUtils {

    private final MinioUtils minioUtils;

    /**
     * save news content image
     *
     * @param url
     *            image url
     * @return image url
     */
    private Mono<String> uploadThumbnail(String url) {
        if (!url.startsWith("data:")) {
            return Mono.just(url);
        }
        String base64Data = url.split(SettingConstant.SPLITER)[1];
        String base64Head = url.split(SettingConstant.SPLITER)[0];

        String extend = base64Head.split(SettingConstant.FILE_SEPARATOR)[1].split(";")[0];
        String path = UUID.randomUUID() + "_." + extend;
        byte[] bytes = Base64.getDecoder().decode(base64Data);
        String returnUrl = minioUtils.getMinioProperties().getPublicUrl() + "/"
                + SettingConstant.MINIO_BUCKET.EZBUY_BUCKET + "/" + path;
        return minioUtils
                .uploadFile(bytes, SettingConstant.MINIO_BUCKET.EZBUY_BUCKET, path)
                .thenReturn(returnUrl);
    }
}
