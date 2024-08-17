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
@Table(name = "option_set")
public class OptionSet extends EntityBase implements Persistable<String> {
    @Id
    private String id; // id cau hinh nhom
    private String code; //ma cau hinh nhom
    private String description; //mo ta cau hinh nhom
//    private Integer status; //trang thai: 0-Khong hieu luc, 1-Hieu luc
//    private String createBy; //nguoi tao
//    private LocalDateTime createAt; //thoi gian tao
//    private String updateBy; //nguoi cap nhat
//    private LocalDateTime updateAt; //thoi gian cap nhat

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
