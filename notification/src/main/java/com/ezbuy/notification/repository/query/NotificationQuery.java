package com.ezbuy.notification.repository.query;

public interface NotificationQuery {
    String insertNotification = "INSERT INTO notification (id, sender, severity, notification_content_id, content_type, create_at, create_by, update_at, update_by, category_id, status, expect_send_time)\n" +
            "VALUES(:id, :sender, :severity, :notificationContentId, :contentType, :createAt, :createBy, :updateAt, :updateBy, :categoryId, :status, :expectSendTime);";
}
