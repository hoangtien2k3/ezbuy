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
@Table(name = "market_page_section")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketPageSection extends EntityBase implements Persistable<String> {

    @Id
    private String id;
    private String pageId;
    private String sectionId;
    private Integer displayOrder;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
