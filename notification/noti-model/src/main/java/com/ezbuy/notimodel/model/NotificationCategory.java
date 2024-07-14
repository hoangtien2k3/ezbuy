package com.ezbuy.notimodel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "notification_category")
public class NotificationCategory {
    private String id;
    private Integer status;
    private String type;
}
