package com.ezbuy.notimodel.model;

import com.ezbuy.notimodel.model.base.EntityBase;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@SuperBuilder
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends EntityBase implements Persistable<String> {

    @Id
    private String id;
    private String sender;
    private String severity;
    private String notificationContentId;
    private LocalDateTime expectSendTime;
    private Integer status;
    private String contentType;
    private String categoryId;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
