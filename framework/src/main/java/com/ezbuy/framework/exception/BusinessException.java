package com.ezbuy.framework.exception;

import java.util.Arrays;

import com.ezbuy.framework.utils.Translator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    private String errorCode;
    private String message;
    private Object[] paramsMsg;

    public BusinessException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = Translator.toLocaleVi(message);
    }

    public BusinessException(String errorCode, String message, String... paramsMsg) {
        this.errorCode = errorCode;
        this.paramsMsg = Arrays.stream(paramsMsg).map(Translator::toLocaleVi).toArray(String[]::new);
        this.message = Translator.toLocaleVi(message, this.paramsMsg);
    }
}
