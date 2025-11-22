package com.ezbuy.noti.model.entity;

import com.ezbuy.noti.model.entity.base.EntityBase;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "notification_content")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationContent extends EntityBase implements Persistable<String> {

    @Id
    private String id;
    private String title;
    private String subTitle;
    private String imageUrl;
    private String url;
    private Integer status;
    private String templateMail;
    private String externalData;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
