/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
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
package com.ezbuy.core.model.response;

import com.ezbuy.core.constants.MessageConstant;
import com.ezbuy.core.util.Translator;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Represents a standardized response structure for API responses.
 *
 * @param <T> the type of the response data
 * @author hoangtien2k3
 */
@Data
@Builder
public class DataResponse<T> implements Serializable {
    private String errorCode;
    private String message;
    private T data;

    public static <T> DataResponse<T> success(T data) {
        return new DataResponse<>(
                MessageConstant.ERROR_CODE_SUCCESS,
                MessageConstant.SUCCESS,
                data
        );
    }

    public static <T> DataResponse<T> failed(T data) {
        return new DataResponse<>(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                MessageConstant.FAIL,
                data
        );
    }

    public DataResponse(String errorCode, String message, T data) {
        this.errorCode = errorCode;
        this.message = Translator.toLocale(message);
        this.data = data;
    }

    public DataResponse(String message, T data) {
        this.message = Translator.toLocale(message);
        this.data = data;
    }

    public DataResponse(String message) {
        this.message = Translator.toLocale(message);
    }
}
