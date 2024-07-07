package com.ezbuy.framework.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidationException extends Exception {
    private Boolean isValid;
    private String errorMessage;
    private String[] paramsMsg;

    public ValidationException(Boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public ValidationException(Boolean isValid, String errorMessage, String... paramsMsg) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
        this.paramsMsg = paramsMsg;
    }
}
