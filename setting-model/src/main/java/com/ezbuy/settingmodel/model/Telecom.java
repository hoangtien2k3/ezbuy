package com.ezbuy.settingmodel.model;

import com.ezbuy.settingmodel.model.base.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "telecom_service")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Telecom extends EntityBase implements Persistable<String> {
    @Id
    private String id;
    private String name;
    private String serviceAlias;
    private String description;
    private String image;
    private String originId;
    private Boolean isFilter;
    private String groupId;
    private String deployOrderCode;
    private String bccsAlias;//alias dich vu ben he thong BCCS_PRODUCT

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
