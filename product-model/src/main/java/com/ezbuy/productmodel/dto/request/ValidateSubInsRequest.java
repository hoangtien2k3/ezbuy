package com.ezbuy.productmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateSubInsRequest {
    private String unitCode;
    private String authorityCode;
    private String businessType;
    private String fiscalYear;
    private String username;
    private String password;
}
