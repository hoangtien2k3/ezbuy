package com.ezbuy.settingmodel.model;

import com.ezbuy.settingmodel.model.base.EntityBase;
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
@Table(name = "option_set_value")
public class OptionSetValue extends EntityBase implements Persistable<String> {
    @Id
    private String id;

    private Long optionSetId; // id cau hinh nhom mapping
    private String code; // ma chi tiet cau hinh nhom
    private String value; // gia tri chi tiet cau hinh nhom
    private String description; // mo ta chi tiet cau hinh nhom

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
