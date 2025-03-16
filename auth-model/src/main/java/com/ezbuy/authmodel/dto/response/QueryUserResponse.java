package com.ezbuy.authmodel.dto.response;

import com.ezbuy.authmodel.dto.PaginationDTO;
import com.ezbuy.authmodel.dto.UserProfileDTO;
import java.util.List;
import lombok.Data;

@Data
public class QueryUserResponse {
    private List<UserProfileDTO> content;
    private PaginationDTO pagination;
}
