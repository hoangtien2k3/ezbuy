package com.ezbuy.authmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePermissionGroup {
    private String groupId;
    private String groupCode;
    private String policyId;
}
