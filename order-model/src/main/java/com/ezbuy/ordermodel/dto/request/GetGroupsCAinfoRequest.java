package com.ezbuy.ordermodel.dto.request;

import lombok.Data;

@Data
public class GetGroupsCAinfoRequest {
    private String idNo;
    private String groupName;
    private String isdn;
    private String groupCode;
    private Long groupId;
    private String organizationId;
    private String groupIDOCS;
    private String groupTypeOCS;
    private String mainIsdnOCS;
    private Integer rowNum; // so luong ban ghi
    private Integer startAt; // bat dau tu ban ghi
}
