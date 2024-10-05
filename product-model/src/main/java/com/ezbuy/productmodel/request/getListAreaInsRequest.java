package com.ezbuy.productmodel.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class getListAreaInsRequest {
    private String username;
    private String password;
    private String areaCode;
}
