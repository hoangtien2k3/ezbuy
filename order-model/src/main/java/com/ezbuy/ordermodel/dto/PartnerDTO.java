package com.ezbuy.ordermodel.dto;


import lombok.Data;

import java.util.List;

@Data
public class PartnerDTO {
    private String documentNumber;
    private Long order;
    private String signType;
    private Boolean signFree;
    private String normal;
    private List<SignerInfoListDTO> signerInfoList;
    private OTPSignRequest otpSignRequest;

    public PartnerDTO(String documentNumber, Long order, String signType, Boolean signFree) {
        this.documentNumber = documentNumber;
        this.order = order;
        this.signType = signType;
        this.signFree = signFree;
    }

    public PartnerDTO() {
    }
}
