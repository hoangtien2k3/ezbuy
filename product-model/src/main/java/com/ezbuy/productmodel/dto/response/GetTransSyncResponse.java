package com.ezbuy.productmodel.dto.response;

import lombok.Data;

@Data
public class GetTransSyncResponse {
    private String transactionId;
    private String state;
}
