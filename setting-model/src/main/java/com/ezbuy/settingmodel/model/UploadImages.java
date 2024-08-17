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
@Table(name = "upload_images")
public class UploadImages extends EntityBase implements Persistable<String> {
    @Id
    private String id;
    private String name;
    private Integer type;
    private String path;
    private String parentId;
//    private Integer status;
//    private LocalDateTime createAt;
//    private String createBy;
//    private LocalDateTime updateAt;
//    private String updateBy;

    @Transient
    private boolean isNew;

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }
}
