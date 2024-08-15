package com.ezbuy.authmodel.dto.response;

import com.ezbuy.authmodel.dto.PaginationDTO;
import com.ezbuy.authmodel.dto.UserProfileDTO;
import lombok.Data;

import java.util.List;

@Data
public class QueryUserResponse {

    private List<UserProfileDTO> content;
    private PaginationDTO pagination;
}
