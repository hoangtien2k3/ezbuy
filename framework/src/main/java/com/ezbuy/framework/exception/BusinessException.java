package com.ezbuy.framework.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    private String errorCode;
    private String message;
    private String[] paramsMsg;

    public BusinessException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BusinessException(String errorCode, String message, String... paramsMsg) {
        this.errorCode = errorCode;
        this.message = message;
        this.paramsMsg = paramsMsg;
    }

}
