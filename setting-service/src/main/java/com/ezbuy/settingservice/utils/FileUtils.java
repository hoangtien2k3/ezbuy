package com.ezbuy.settingservice.utils;

import com.ezbuy.framework.utils.MinioUtils;
import com.ezbuy.settingservice.constant.SettingConstant;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
public class FileUtils {

    private final MinioUtils minioUtils;

    /**
     * save news content image
     * @param url image url
     * @return image url
     */
    private Mono<String> uploadThumbnail(String url) {
        if (!url.startsWith("data:")) {
            return Mono.just(url);
        }
        String base64Data = url.split(",")[1];
        String base64Head = url.split(",")[0];

        String extend = base64Head.split("/")[1].split(";")[0];
        String path = UUID.randomUUID() + "_." + extend;
        byte[] bytes = Base64.getDecoder().decode(base64Data);
        String returnUrl = minioUtils.getMinioProperties().getPublicUrl() +
                "/" + SettingConstant.MINIO_BUCKET.SME_PUBLIC +
                "/" + path;
        return minioUtils.uploadFile(bytes, SettingConstant.MINIO_BUCKET.SME_PUBLIC, path)
                .thenReturn(returnUrl);
    }
}
