package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class OTPSignRequest {
    private String signAllowContent; //log phap che
    private String extendContentOTPSMS; //noi dung gui kem sms
}
