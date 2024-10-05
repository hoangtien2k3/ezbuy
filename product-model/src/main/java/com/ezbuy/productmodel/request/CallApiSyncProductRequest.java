package com.ezbuy.productmodel.request;

import com.ezbuy.productmodel.dto.ChangeDataDTO;
import com.ezbuy.productmodel.dto.ServiceSyncProductDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CallApiSyncProductRequest {
    private String organizationId; //id to chuc
    private String idNo; //so giay to
    private String action; //loai tac dong
    private ServiceSyncProductDTO service;
    private List<ChangeDataDTO> object;
    private String transactionId;//id cua request sync
    private String userName;//Username tac dong ban ghi
}
