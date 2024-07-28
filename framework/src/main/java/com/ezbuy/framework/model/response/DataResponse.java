package com.ezbuy.framework.model.response;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.ezbuy.framework.constants.MessageConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a standardized response structure for API responses.
 *
 * @param <T> the type of the response data
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponse<T> implements Serializable {
    private String errorCode;
    private String message;
    private T data;

    /**
     * Constructs a DataResponse with a message and data.
     *
     * @param message the message to be included in the response
     * @param data the data to be included in the response
     */
    public DataResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    /**
     * Constructs a DataResponse with a message.
     *
     * @param message the message to be included in the response
     */
    public DataResponse(String message) {
        this.message = message;
    }

    /**
     * Creates a successful DataResponse with the provided data.
     *
     * @param data the data to be included in the response
     * @param <T> the type of the response data
     * @return a DataResponse indicating success
     */
    public static <T> DataResponse<T> success(T data) {
        return DataResponse.<T>builder()
                .errorCode(MessageConstant.ERROR_CODE_SUCCESS)
                .message(MessageConstant.SUCCESS)
                .data(data)
                .build();
    }

    /**
     * Creates a failed DataResponse with the provided data.
     *
     * @param data the data to be included in the response
     * @param <T> the type of the response data
     * @return a DataResponse indicating failure
     */
    public static <T> DataResponse<T> failed(T data) {
        return DataResponse.<T>builder()
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(MessageConstant.FAIL)
                .data(data)
                .build();
    }
}
