package com.ezbuy.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePermissionRequest {
    private String clientId;
    private String roleId;
    private String roleCode;
    private String policyId;
    private List<EmployeePermissionGroup> permissionGroupList;
}
