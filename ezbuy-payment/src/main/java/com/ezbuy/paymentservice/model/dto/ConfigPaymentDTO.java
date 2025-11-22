package com.ezbuy.paymentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigPaymentDTO {
    private String merchant;
    private String accessCode;
    private String hashCode;
}
