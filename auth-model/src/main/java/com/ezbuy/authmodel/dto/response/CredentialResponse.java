package com.ezbuy.authmodel.dto.response;

import lombok.Data;

@Data
public class CredentialResponse {
    private String credentialId;
    private String certificate;
    private String status;
    private String validFrom;
    private String validTo;
    private String serialNumber;
    private String subjectDN;
}
