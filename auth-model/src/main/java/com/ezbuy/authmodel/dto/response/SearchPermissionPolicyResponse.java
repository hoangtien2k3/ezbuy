package com.ezbuy.authmodel.dto.response;

import com.ezbuy.authmodel.dto.PaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPermissionPolicyResponse {
    private List<PermissionPolicyDetailDto> content;
    private PaginationDTO pagination;
}
