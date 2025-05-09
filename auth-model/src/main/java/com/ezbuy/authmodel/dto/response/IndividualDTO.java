package com.ezbuy.authmodel.dto.response;

import com.ezbuy.authmodel.model.Individual;
import com.ezbuy.authmodel.model.TenantIdentify;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualDTO extends Individual {
    // @Valid
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
