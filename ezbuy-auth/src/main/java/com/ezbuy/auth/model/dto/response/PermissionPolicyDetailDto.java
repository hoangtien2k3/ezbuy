package com.ezbuy.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionPolicyDetailDto {
    private String id;
    private String type;
    private String code;
    private String name;
    private String description;
    private String individualOrganizationPermissionsId;
    private Integer state;
    private String keycloakId;
    private String keycloakName;
    private String policyId;
    private String clientId;
}
