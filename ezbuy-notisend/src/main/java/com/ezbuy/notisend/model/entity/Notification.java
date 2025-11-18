package com.ezbuy.notisend.model.entity;

import com.ezbuy.notisend.model.entity.base.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

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
