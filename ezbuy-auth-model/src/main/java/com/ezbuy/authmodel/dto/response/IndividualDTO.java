package com.ezbuy.authmodel.dto.response;

import com.ezbuy.authmodel.model.Individual;
import com.ezbuy.authmodel.model.TenantIdentify;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IndividualDTO extends Individual {
    private List<TenantIdentify> identifies;
    private String organizationUnitId;
}
