package com.ezbuy.notiservice.repository.query;

public interface NotificationContentQuery {

    String insertNotificationContent = "INSERT INTO notification_content (id, title, sub_title, image_url, create_at, create_by, update_at, update_by, url, status)\n" +
            "VALUES (:id, :title, :subTitle, :imageUrl, :createAt, :createBy, :updateAt, :updateBy, :url, :status);";
}
