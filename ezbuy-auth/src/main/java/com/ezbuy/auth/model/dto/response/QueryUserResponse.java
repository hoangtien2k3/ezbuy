package com.ezbuy.auth.model.dto.response;

import com.ezbuy.auth.model.dto.PaginationDTO;
import com.ezbuy.auth.model.dto.UserProfileDTO;
import java.util.List;
import lombok.Data;

@Data
public class QueryUserResponse {
    private List<UserProfileDTO> content;
    private PaginationDTO pagination;
}
