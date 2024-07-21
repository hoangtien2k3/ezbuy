package com.ezbuy.framework.exception;

import com.ezbuy.framework.utils.Translator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    private String errorCode;
    private String message;
    private String[] paramsMsg;

    public BusinessException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = Translator.toLocaleVi(message);
    }

    public BusinessException(String errorCode, String message, String... paramsMsg) {
        this.errorCode = errorCode;
        this.paramsMsg = Arrays.stream(paramsMsg).map(Translator::toLocaleVi).toArray(String[]::new);
        this.message = Translator.toLocaleVi(message,  this.paramsMsg);
    }
}
