package com.ezbuy.authmodel.dto.request;

import com.ezbuy.authmodel.dto.response.IndividualDTO;
import com.ezbuy.authmodel.model.TenantIdentify;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequest extends OrganizationRequest {
    @Valid
    private IndividualDTO representative;

    @Valid
    private List<TenantIdentify> identifies;
}
