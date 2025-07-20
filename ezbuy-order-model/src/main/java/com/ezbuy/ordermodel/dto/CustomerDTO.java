package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    private String name; // x

    private String contactEmail; // x

    private String telMobile; // x

    private String areaCode; // x

    private String district; // x

    private String precinct; // x

    private String address; // x

    private String province; // x

    private String portalAccountId; // không có thông tin này

    // thiếu thông tin giấy phép kinh doanh, mã số thuế, ngày cấp, nơi cấp

    private String position; // chuc vu
}
