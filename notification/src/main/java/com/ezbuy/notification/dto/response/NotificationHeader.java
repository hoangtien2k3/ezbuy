package com.ezbuy.notification.dto.response;

import com.ezbuy.notification.model.NotificationContent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NotificationHeader extends NotificationContent {
    private String state;

    public NotificationHeader(
            String id,
            String title,
            String subTitle,
            String imageUrl,
            String url,
            Integer status,
            String state,
            String externalData) {
        super(id, title, subTitle, imageUrl, url, status, externalData);
        this.state = state;
    }
}
