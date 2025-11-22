package com.ezbuy.auth.model.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
