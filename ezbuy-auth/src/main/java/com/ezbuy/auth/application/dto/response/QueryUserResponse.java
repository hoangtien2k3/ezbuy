package com.ezbuy.auth.application.dto.response;

import com.ezbuy.auth.application.dto.PaginationDTO;
import com.ezbuy.auth.application.dto.UserProfileDTO;
import java.util.List;
import lombok.Data;

@Data
public class QueryUserResponse {
    private List<UserProfileDTO> content;
    private PaginationDTO pagination;
}
