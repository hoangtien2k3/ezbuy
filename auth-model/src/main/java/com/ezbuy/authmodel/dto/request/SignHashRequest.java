package com.ezbuy.authmodel.dto.request;

import lombok.Data;

@Data
public class SignHashRequest {
    private String file;
    private String credentialID;
    private String certificate;
    private String idNo;
}
