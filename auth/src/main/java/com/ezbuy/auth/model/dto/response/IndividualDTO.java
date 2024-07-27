package com.ezbuy.auth.model.dto.response;

import java.util.List;

import com.ezbuy.auth.model.postgresql.Individual;
import com.ezbuy.auth.model.postgresql.TenantIdentify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualDTO extends Individual {
    //    @Valid
    private List<TenantIdentify> identifies;

    public IndividualDTO(Individual individual) {
        super(individual);
    }

    public IndividualDTO(Individual individual, List<TenantIdentify> tenantIdentifies) {
        super(individual);
        this.identifies = tenantIdentifies;
    }

    private String organizationUnitId;
}
