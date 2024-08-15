package com.ezbuy.authmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class request for migrate role admin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobAddRoleAdminForOldUserRequest {
     private String roleId;   // id of role in keycloak
     private String roleName; // name, code of role in keycloak
     private String clientId; // clientId cua dich vu
     private String policyId; // policyId cua dich vu
     private Integer offset;  // offset to migrate
     private Integer limit;   // limit of moi offset
     private String username; // username of user
}
