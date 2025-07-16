package com.ezbuy.notiservice.repository.query;

public interface TransmissionQuery {

    String GET_COUNT_NOTICE_DTO = """
                SELECT 
                  c.type as type, 
                  COUNT(state) AS quantity 
                FROM 
                  sme_notification.transmission tr 
                  INNER JOIN notification n ON tr.notification_id = n.id 
                  INNER JOIN notification_category c on n.category_id = c.id 
                  INNER JOIN channel c2 on tr.channel_id = c2.id 
                WHERE 
                  state IN ('NEW') 
                  AND c2.type = 'REST' 
                  AND tr.receiver = : receiver 
                  AND tr.status = 1 
                  AND n.status = 1 
                  AND c2.status = 1 
                GROUP BY 
                  c.type;
            """;

    String GET_ALL_BY_NOTIFICATION_BY_CATEGORY_TYPE = """
                SELECT 
                  nc.*, 
                  tr.state 
                FROM 
                  notification_content nc 
                  INNER JOIN notification n ON n.notification_content_id = nc.id 
                  INNER JOIN notification_category nca ON n.category_id = nca.id 
                  INNER JOIN transmission tr ON tr.notification_id = n.id 
                  INNER JOIN channel c ON tr.channel_id = c.id 
                WHERE 
                  tr.receiver = : receiver 
                  AND tr.status = 1 
                  AND tr.state IN ('NEW', 'UNREAD', 'READ') 
                  AND nc.status = 1 
                  AND n.status = 1 
                  AND nca.status = 1 
                  AND c.status = 1 
                  AND c.type = 'REST' 
                  AND nca.type = : categoryType 
                ORDER BY 
                  : sort 
                LIMIT 
                  : pageSize OFFSET : index;
            """;

    String CHANGE_STATE_TRANSMISSION_BY_TYPE = """
                UPDATE 
                  transmission tr 
                SET 
                  state = : state, 
                  update_at = CURRENT_TIMESTAMP, 
                  update_by = 'system' 
                WHERE 
                  receiver = : receiver 
                  AND EXISTS (
                    SELECT 
                      1 
                    FROM 
                      notification n 
                      INNER JOIN notification_content c ON n.notification_content_id = c.id 
                    WHERE 
                      c.id = : notificationContentId 
                      AND n.id = tr.notification_id
                  );
            """;

    String INSERT_TRANSMISSION = """
                INSERT INTO transmission (
                  id, receiver, state, status, resend_count, 
                  create_at, create_by, update_at, 
                  update_by, channel_id, notification_id
                ) 
                VALUES 
                  (
                    : id, : receiver, : state, : status, : resendCount, 
                    : createAt, : createBy, : updateAt, 
                    : updateBy, : channelId, : notificationId
                  );
            """;

    String GET_ALL_NOTIFICATION_CONTENT_BY_CREATE_AT_AFTER = """
                SELECT 
                  nc.* 
                FROM 
                  notification_content nc 
                  INNER JOIN notification n ON nc.id = n.notification_content_id 
                  INNER JOIN transmission tr ON tr.notification_id = n.id 
                  INNER JOIN channel ch ON ch.id = tr.channel_id 
                WHERE 
                  tr.receiver = : receiver 
                  AND tr.create_at > : newestNotiTime 
                  AND tr.status = 1 
                  AND tr.state = 'NEW' 
                  AND nc.status = 1 
                  AND n.status = 1 
                  AND ch.status = 1 
                  AND ch.type = 'REST' 
                ORDER BY 
                  tr.create_at DESC;
            """;

    String GET_TRANSMISSIONS_TO_SEND_MAIL = """
                SELECT 
                  tr.id, 
                  tr.receiver, 
                  no.sender, 
                  ch.type, 
                  noc.title, 
                  noc.sub_title 
                FROM 
                  transmission tr 
                  INNER JOIN channel ch ON tr.channel_id = ch.id 
                  INNER JOIN notification no ON tr.notification_id = no.id 
                  INNER JOIN notification_content noc ON no.notification_content_id = noc.id 
                WHERE 
                  tr.state IN ('PENDING', 'FAILED') 
                  AND (
                    no.expect_send_time IS NULL 
                    OR now() > no.expect_send_time
                  ) 
                  AND tr.resend_count <= : resendCount 
                  AND ch.type IN ('EMAIL', 'REST') 
                  AND tr.status = 1 
                  AND no.status = 1 
                  AND ch.status = 1;
            """;

    String UPDATE_TRANSMISSION_STATE = """
                UPDATE 
                  transmission 
                SET 
                  state = 'NEW', 
                  update_at = CURRENT_TIMESTAMP, 
                  update_by = 'system' 
                WHERE 
                  id IN (: transmissionIds);
            """;

    String UPDATE_TRANSMISSION_STATE_AND_RESEND_COUNT = """
                UPDATE 
                  transmission 
                SET 
                  state = 'FAILED', 
                  resend_count = resend_count + 1, 
                  update_at = CURRENT_TIMESTAMP, 
                  update_by = 'system' 
                WHERE 
                  id = ANY (: transmissionIds);
            """;

    String GET_TRANSMISSION_BY_NOTIFICATION_CONTENT_ID = """
                SELECT 
                  tr.id 
                FROM 
                  transmission tr 
                  INNER JOIN notification n ON tr.notification_id = n.id 
                  INNER JOIN notification_content nc ON n.notification_content_id = nc.id 
                WHERE 
                  nc.id = : notificationContentId 
                  AND tr.status = 1 
                  AND n.status = 1 
                  AND nc.status = 1 
                  AND tr.receiver = : receiver;
            """;

    String FIND_BY_ID_AND_STATUS = """
                SELECT * 
                FROM 
                  transmission 
                WHERE 
                  id = : id 
                  AND status = : status;
            """;

    String UPDATE_STATE_BY_ID = """
                UPDATE 
                  transmission 
                SET 
                  state = : state, 
                  update_by = 'system' 
                WHERE 
                  id = : id;
            """;

    String FIND_ALL_USER_TRANSMISSION_FROM_TO = """
                SELECT
                  t.id,
                  t.email,
                  t.create_at,
                  t.create_by,
                  t.state,
                  c.template_mail
                FROM =
                  transmission t
                  INNER JOIN notification n ON t.notification_id = n.id
                  INNER JOIN notification_content c ON n.notification_content_id = c.id
                WHERE 1 = 1
            """;

    String FIND_COUNT_USER_TRANSMISSION_FROM_TO = """
                SELECT 
                  t.id, 
                  t.email, 
                  t.create_at, 
                  t.create_by, 
                  t.state, 
                  c.template_mail 
                FROM 
                  transmission t 
                  INNER JOIN notification n ON t.notification_id = n.id 
                  INNER JOIN notification_content c ON n.notification_content_id = c.id 
                WHERE 1 = 1
            """;

}
