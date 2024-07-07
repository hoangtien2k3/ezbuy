package com.ezbuy.framework.model.response;

import com.ezbuy.framework.constants.MessageConstant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponse<T> implements Serializable {
    private String errorCode;
    private String message;
    private T data;

    public DataResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public DataResponse(String message) {
        this.message = message;
    }

    public static <T> DataResponse<T> success(T data) {
        return DataResponse.<T>builder()
                .message(MessageConstant.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> DataResponse<T> failed(T data) {
        return DataResponse.<T>builder()
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(MessageConstant.FAIL)
                .data(data)
                .build();
    }
}
