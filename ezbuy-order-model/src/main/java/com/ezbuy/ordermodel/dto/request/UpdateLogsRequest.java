package com.ezbuy.ordermodel.dto.request;

import lombok.Data;

@Data
public class UpdateLogsRequest {

    private String orderId;
    private String logs;
}
