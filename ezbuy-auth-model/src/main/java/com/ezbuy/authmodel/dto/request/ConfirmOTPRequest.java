package com.ezbuy.authmodel.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOTPRequest {
    @NotEmpty(message = "dto.otp.email.not.empty")
    @Size(max = 200, message = "dto.otp.email.over.length")
    private String email;
    // @NotEmpty(message = "dto.otp.not.empty")
    private String otp;

    @Size(max = 50, message = "dto.otp.type.over.length")
    @NotEmpty(message = "dto.otp.type.not.empty")
    private String type;
}
