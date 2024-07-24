package com.ezbuy.notification.model;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import com.ezbuy.notification.model.base.EntityBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends EntityBase {
    private String id;
    private String sender;
    private String severity;
    private String notificationContentId;
    private LocalDateTime expectSendTime;
    private Integer status;
    private String contentType;
    private String categoryId;
}
