package com.ezbuy.framework.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraceErrorResponse<T> extends DataResponse<T> {
    private String requestId;

    public TraceErrorResponse(String errorCode, String message, T data, String requestId) {
        super(errorCode, message, data);
        this.requestId = requestId;
    }
}
