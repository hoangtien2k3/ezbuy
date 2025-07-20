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
@Table(name = "page")
public class Page extends EntityBase implements Persistable<String> {
    @Id
    private String id;

    private String code;
    private String title;
    private String logoUrl;

    @Transient
    private boolean insert;

    @Override
    public boolean isNew() {
        return insert || id == null;
    }
}
