package com.ezbuy.auth.application.dto.request;

import com.ezbuy.auth.application.dto.response.IndividualDTO;
import com.ezbuy.auth.domain.model.entity.TenantIdentifyEntity;
import jakarta.validation.Valid;
import java.util.List;
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
    private List<TenantIdentifyEntity> identifies;
}
