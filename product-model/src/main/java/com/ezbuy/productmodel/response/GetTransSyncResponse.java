package com.ezbuy.productmodel.response;

import lombok.Data;

@Data
public class GetTransSyncResponse {
    private String transactionId;
    private String state;
}
