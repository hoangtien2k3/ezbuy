package com.ezbuy.settingservice.utils;

import com.ezbuy.settingservice.constant.SettingConstant;
import com.reactify.util.MinioUtils;
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
