package com.ezbuy.auth.model.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
