package com.ezbuy.productmodel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListAreaInsDataDTO {
    private String areaCode;
    private String name;
    private Long areaLevel;
    private String authorityCode;
    private String authorityName;
}
