package com.ezbuy.auth.model.dto.response;

import com.ezbuy.auth.model.entity.IndividualEntity;
import com.ezbuy.auth.model.entity.TenantIdentifyEntity;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IndividualDTO extends IndividualEntity {
    private List<TenantIdentifyEntity> identifies;
    private String organizationUnitId;
}
