package com.ezbuy.auth.application.dto.response;

import com.ezbuy.auth.domain.model.entity.IndividualEntity;
import com.ezbuy.auth.domain.model.entity.TenantIdentifyEntity;
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
