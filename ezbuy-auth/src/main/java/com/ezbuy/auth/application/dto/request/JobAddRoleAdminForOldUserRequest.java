package com.ezbuy.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobAddRoleAdminForOldUserRequest {
    private String roleId;
    private String roleName;
    private String clientId;
    private String policyId;
    private Integer offset;
    private Integer limit;
    private String username;
}
