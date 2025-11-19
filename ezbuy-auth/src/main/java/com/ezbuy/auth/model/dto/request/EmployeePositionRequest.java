package com.ezbuy.auth.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePositionRequest {
    private String organizationUnitId;
    private String positionId;
    private String managerId;
}
