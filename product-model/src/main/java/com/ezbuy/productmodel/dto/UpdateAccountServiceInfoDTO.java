package com.ezbuy.productmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountServiceInfoDTO {
    private Long telecomServiceId; // ma dich vu
    private String idNo; // so giay to
    private String isdn; // thue bao
    private String telecomServiceAlias;
}
