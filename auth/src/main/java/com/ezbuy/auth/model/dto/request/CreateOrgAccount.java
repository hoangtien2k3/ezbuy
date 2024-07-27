package com.ezbuy.auth.model.dto.request;

import com.ezbuy.auth.model.dto.response.IndividualDTO;
import com.ezbuy.auth.model.postgresql.TenantIdentify;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrgAccount {
    @NotEmpty(message = "dto.email.not.empty")
    @Size(max = 200, message = "dto.email.over.length")
    private String email;

    @Length(max = 12, message = "organization.phone.over.length")
    @NotEmpty(message = "organization.phone.not.empty")
    private String phone;

    @Length(max = 255, message = "organization.name.over.length")
    @NotEmpty(message = "organization.name.not.empty")
    private String name;

    private LocalDateTime foundingDate;

    @NotNull(message = "organization.representative.null")
    private IndividualDTO representative;

    @NotNull(message = "organization.identify.null")
    private List<TenantIdentify> identifies;
}
