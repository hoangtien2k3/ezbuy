package com.ezbuy.productmodel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateSubInsResponse {
    private String errorCode;
    private String description;
    private Object data;
}
