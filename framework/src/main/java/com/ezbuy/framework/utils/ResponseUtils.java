package com.ezbuy.framework.utils;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.model.response.DataResponse;

import reactor.core.publisher.Mono;

public class ResponseUtils {

    public static ResponseEntity<DataResponse> ok(Object data) {
        return ResponseEntity.ok(new DataResponse(Translator.toLocale("success"), data));
    }

    public static ResponseEntity<DataResponse> ok(Object data, String message) {
        return ResponseEntity.ok(new DataResponse(Translator.toLocale(message), data));
    }

    public static Mono<Object> getResponse(Optional<DataResponse> response, String extraInfo) {
        return getResponseWithoutData(response, extraInfo)
                .switchIfEmpty(Mono.error(new BusinessException(
                        CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " data is null " + response)))
                .flatMap(data -> {
                    if (data == null) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " data is null " + response));
                    }
                    return Mono.just(data);
                });
    }

    public static Mono<Object> getResponseWithoutData(Optional<DataResponse> response, String extraInfo) {
        if (response.isEmpty()) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " response empty"));
        }
        var data = response.get();
        if (data.getErrorCode() != null) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " errorCode exist " + data.getErrorCode()));
        }
        return Mono.justOrEmpty(data.getData());
    }
}
