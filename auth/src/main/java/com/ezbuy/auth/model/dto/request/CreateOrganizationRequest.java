package com.ezbuy.auth.model.dto.request;

import java.util.List;

import jakarta.validation.Valid;

import com.ezbuy.auth.model.dto.response.IndividualDTO;
import com.ezbuy.auth.model.postgresql.TenantIdentify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequest extends OrganizationRequest {
    @Valid
    private IndividualDTO representative;

    @Valid
    private List<TenantIdentify> identifies;
}
