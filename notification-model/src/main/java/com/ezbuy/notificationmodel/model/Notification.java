package com.ezbuy.notificationmodel.model;

import com.ezbuy.notificationmodel.model.base.EntityBase;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

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
