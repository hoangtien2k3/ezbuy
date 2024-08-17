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
@Table(name = "setting")
public class Setting extends EntityBase implements Persistable<String>{
    @Id
    private String id;
    private String code;
    private String value;
    private String description;
//    private Integer status;
//    private String createBy;
//    private String updateBy;
//    private LocalDateTime createAt;
//    private LocalDateTime updateAt;

    @Transient
    private boolean isNew = false;
    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
