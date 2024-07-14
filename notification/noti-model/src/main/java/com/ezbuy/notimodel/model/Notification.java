package com.ezbuy.notimodel.model;

import com.ezbuy.notimodel.model.base.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Table;
import java.time.LocalDateTime;

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
