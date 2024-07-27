package com.ezbuy.auth.model.dto.request;

import com.ezbuy.auth.model.dto.response.IndividualDTO;
import com.ezbuy.auth.model.postgresql.TenantIdentify;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequest extends OrganizationRequest{
    @Valid
    private IndividualDTO representative;

    @Valid
    private List<TenantIdentify> identifies;
}
