package com.ezbuy.notiservice.repository.query;

public interface NotificationQuery {
    String INSERT_NOTIFICATION = """
            INSERT INTO notification (
              id, sender, severity, notification_content_id, 
              content_type, create_at, create_by, 
              update_at, update_by, category_id, 
              status, expect_send_time
            ) 
            VALUES 
              (
                : id, : sender, : severity, : notificationContentId, 
                : contentType, : createAt, : createBy, 
                : updateAt, : updateBy, : categoryId, 
                : status, : expectSendTime
              );
        """;
}
