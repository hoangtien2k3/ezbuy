package com.ezbuy.auth.application.dto.response;

import com.ezbuy.auth.application.dto.PaginationDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPermissionPolicyResponse {
    private List<PermissionPolicyDetailDto> content;
    private PaginationDTO pagination;
}
