package com.ezbuy.settingservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ContentNews extends EntityBase implements Persistable<String> {
    @Id
    private String id;

    private String sourceType;
    private Integer newsType;
    private String image;
    private String title;
    private String content;
    private String path;

    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return newRecord || id == null;
    }
}
