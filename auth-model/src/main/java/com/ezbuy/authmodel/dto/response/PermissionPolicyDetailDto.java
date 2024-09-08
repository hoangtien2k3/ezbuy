/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authmodel.dto.response;

import com.ezbuy.authmodel.model.PermissionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
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

    public static PermissionPolicyDetailDto entityToDto(PermissionPolicy permissionPolicy) {
        PermissionPolicyDetailDto permissionPolicyDetailDto = new PermissionPolicyDetailDto();
        // permissionPolicyDetailDto.setName(permissionPolicy.getValue());
        permissionPolicyDetailDto.setType(permissionPolicy.getType());
        permissionPolicyDetailDto.setCode(permissionPolicy.getCode());
        permissionPolicyDetailDto.setIndividualOrganizationPermissionsId(
                permissionPolicy.getIndividualOrganizationPermissionsId());
        // permissionPolicyDetailDto.setDescription(permissionPolicy.getDescription());
        permissionPolicyDetailDto.setState(permissionPolicy.getStatus());
        return permissionPolicyDetailDto;
    }

    public static List<PermissionPolicyDetailDto> entityToDto(List<PermissionPolicy> permissionPolicyList) {
        if (CollectionUtils.isEmpty(permissionPolicyList)) return new ArrayList<>();
        return permissionPolicyList.stream()
                .map(PermissionPolicyDetailDto::copyEntityToDto)
                .collect(Collectors.toList());
    }

    public static PermissionPolicyDetailDto copyEntityToDto(PermissionPolicy permissionPolicy) {
        PermissionPolicyDetailDto dto = new PermissionPolicyDetailDto();
        try {
            BeanUtils.copyProperties(dto, permissionPolicy);
        } catch (Exception e) {
            log.error("IndividualUnitPositionDto entityToDto error", e);
        }
        return dto;
    }
}
