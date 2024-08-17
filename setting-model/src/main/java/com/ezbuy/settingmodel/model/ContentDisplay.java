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

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentDisplay extends EntityBase implements Persistable<String> {
    @Id
    private String id;
    private String pageId;
    private String type;
    private String content;
    private String telecomServiceId;
    private String serviceAlias;
    private String title;
    private String subtitle;
    private String icon;
    private String image;
    private String backgroundImage;
    private Integer displayOrder;
    private String refUrl;
    private String parentId;
    private Boolean isOriginal;
    private String name;
    private String description;
    private String screenUrl;

    @Transient
    private boolean insert;

    @Override
    public boolean isNew() {
        return insert || id == null;
    }
}
