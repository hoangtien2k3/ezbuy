package com.ezbuy.authmodel.dto.request;

import com.ezbuy.authmodel.dto.BusinessRegisInforDto;
import com.ezbuy.authmodel.dto.PresentativeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationUnitRequest {
    private String organizationUnitId;
    private String name;
    private String shortName;
    private String parentId;
    private String code;
    private String fieldOfActivity;
    private String unitTypeId;
    private String address;
    private String description;
    private String leaderId;
    private String placeName;
    private String leaderName;
    private String unitTypeName;
    private BusinessRegisInforDto businessRegistrationInfor;
    private PresentativeDto presentative;
}
