package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class ErrorDTO {
    String errorMsg;
    String objectId;
    String errorCode;
}
