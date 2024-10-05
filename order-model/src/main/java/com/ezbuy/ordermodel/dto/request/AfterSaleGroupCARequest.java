package com.ezbuy.ordermodel.dto.request;

import com.ezbuy.ordermodel.dto.AddGroupCaDTO;
import lombok.Data;

import java.util.List;

@Data
public class AfterSaleGroupCARequest {
    private String groupCode;
    private Long groupId;
    private String isdn;
    private Long totalSign;//Tong so luong ky
    private String organizationId;
    private List<AddGroupCaDTO> lstSubscriberCA;
}
