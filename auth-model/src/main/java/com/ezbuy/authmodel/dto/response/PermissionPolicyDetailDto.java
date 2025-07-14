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
        } catch (Exception ignored) {
        }
        return dto;
    }
}
