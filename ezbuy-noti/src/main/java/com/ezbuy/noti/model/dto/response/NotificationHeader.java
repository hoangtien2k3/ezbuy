package com.ezbuy.noti.model.dto.response;

import com.ezbuy.noti.model.entity.NotificationContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NotificationHeader extends NotificationContent {
    private String state;
}
