package com.ezbuy.settingmodel.model;

import com.ezbuy.settingmodel.model.base.EntityBase;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "market_section")
public class MarketSection extends EntityBase implements Persistable<String> {
    @Id
    private String id;

    private String type;
    private String code;
    private String name;
    private String description;
    private Long displayOrder;
    private String data;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
