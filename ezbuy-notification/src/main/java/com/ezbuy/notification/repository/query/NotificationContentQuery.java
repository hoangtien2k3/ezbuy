package com.ezbuy.notification.repository.query;

public interface NotificationContentQuery {

    String INSERT_NOTIFICATION_CONTENT = """
                INSERT INTO notification_content (
                  id, title, sub_title, image_url, create_at,
                  create_by, update_at, update_by,
                  url, status
                )
                VALUES
                  (
                    : id, : title, : subTitle, : imageUrl,
                    : createAt, : createBy, : updateAt,
                    : updateBy, : url, : status
                  );
            """;

    String GET_NOTIFICATION_CONTENT_LIST_BY_CATEGORY_TYPE = """
                SELECT 
                  nc.*, 
                  tr.state 
                FROM notification_content nc 
                  INNER JOIN notification n ON n.notification_content_id = nc.id 
                  INNER JOIN notification_category nca ON n.category_id = nca.id 
                  INNER JOIN transmission tr ON tr.notification_id = n.id 
                  INNER JOIN channel c ON tr.channel_id = c.id 
                WHERE tr.receiver = : receiver 
                  AND tr.status = 1 
                  AND tr.state IN ('NEW', 'UNREAD', 'READ') 
                  AND nc.status = 1 
                  AND n.status = 1 
                  AND nca.status = 1 
                  AND c.status = 1 
                  AND c.type = 'REST' 
                  AND nca.type = : categoryType 
                ORDER BY % s 
                LIMIT : pageSize OFFSET : index
            """;

}
