package com.ezbuy.authmodel.dto.request;

import java.util.List;

import jakarta.validation.Valid;

import com.ezbuy.authmodel.dto.response.IndividualDTO;
import com.ezbuy.authmodel.model.TenantIdentify;

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
