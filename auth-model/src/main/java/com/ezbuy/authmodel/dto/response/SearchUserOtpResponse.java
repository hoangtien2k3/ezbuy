package com.ezbuy.authmodel.dto.response;

import com.ezbuy.authmodel.dto.PaginationDTO;
import com.ezbuy.authmodel.model.UserOtp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUserOtpResponse {
    private List<UserOtp> userOtps;
    private PaginationDTO pagination;
}
