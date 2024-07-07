package com.ezbuy.framework.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraceErrorResponse<T> extends DataResponse {
    private String requestId;

    public TraceErrorResponse(String errorCode, String message, Object data, String requestId) {
        super(errorCode, message, data);
        this.requestId = requestId;
    }
}
