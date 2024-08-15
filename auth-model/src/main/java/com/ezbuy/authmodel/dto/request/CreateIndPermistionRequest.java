package com.ezbuy.authmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIndPermistionRequest {
    private String groupPermissionName;
    private String groupPermissionCode;
    private Integer state;
    private String description;
    private String type;
    private String value;
    private UUID individualOrgPermissionsId;
    private String action;
    private UUID individualPermissionsId;
}
