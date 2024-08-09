package com.ezbuy.notiservice.repository.query;

public interface TransmissionQuery {
    String getCountNoticeDTO = "SELECT c.type as type, COUNT(state) AS quantity "
            + "FROM sme_notification.transmission tr "
            + "INNER JOIN notification n ON tr.notification_id = n.id "
            + "INNER JOIN notification_category c on n.category_id = c.id "
            + "INNER JOIN channel c2 on tr.channel_id = c2.id "
            + "WHERE  "
            + "state IN ('NEW') "
            + "AND c2.type = 'REST' "
            + "AND tr.receiver = :receiver "
            + "AND tr.status = 1 "
            + "AND n.status = 1 "
            + "AND c2.status =1 "
            + "GROUP BY c.type;";

    String getAllByNotificationByCategoryType = "SELECT nc.*,tr.state \n"
            + "FROM notification_content nc\n"
            + "INNER JOIN notification n\n"
            + "ON n.notification_content_id = nc.id\n"
            + "INNER JOIN notification_category nca\n"
            + "ON n.category_id = nca.id\n"
            + "INNER JOIN transmission tr\n"
            + "ON tr.notification_id = n.id\n"
            + "INNER JOIN channel c\n"
            + "ON tr.channel_id = c.id\n"
            + "where tr.receiver = (:receiver)  \n"
            + "AND tr.status =1\n"
            + "AND tr.state IN ('NEW','UNREAD','READ')\n"
            + "AND nc.status =1\n"
            + "AND n.status =1\n"
            + "AND nca.status =1\n"
            + "AND c.status =1\n"
            + "AND c.type = 'REST'\n"
            + "AND nca.type = (:categoryType) \n"
            + "ORDER BY :sort \n"
            + "LIMIT :pageSize \n"
            + "OFFSET :index;";

    String changeStateTransmissionByType = "UPDATE transmission tr\n"
            + "SET tr.state = :state , tr.update_at= Now(), tr.update_by='system'\n"
            + "WHERE tr.receiver = :receiver\n"
            + "AND tr.notification_id = (SELECT notification.id\n"
            + "                          from notification\n"
            + "                          INNER JOIN notification_content c\n"
            + "                          on notification.notification_content_id = c.id\n"
            + "                          Where c.id = :notificationContentId\n"
            + "                          )\n";

    String insertTransmission =
            "INSERT INTO transmission (id, receiver, state, status, resend_count, create_at, create_by, update_at, update_by, channel_id, notification_id)\n"
                    + "VALUES(:id, :receiver, :state, :status, :resendCount, :createAt, :createBy, :updateAt, :updateBy, :channelId, :notificationId);";

    String getAllNotificationContentByCreateAtAfter = "SELECT nc.* FROM notification_content nc\n"
            + "INNER JOIN notification n\n"
            + "ON nc.id = n.notification_content_id\n"
            + "INNER JOIN transmission tr\n"
            + "ON tr.notification_id = n.id\n"
            + "INNER JOIN channel ch\n"
            + "ON ch.id = tr.channel_id\n"
            + "WHERE tr.receiver = :receiver\n"
            + "AND tr.create_at > :newestNotiTime\n"
            + "AND tr.status =1\n"
            + "AND tr.state= 'NEW'\n"
            + "AND nc.status=1 \n"
            + "AND n.status =1\n"
            + "AND ch.status =1 \n"
            + "AND ch.type='REST'\n"
            + "ORDER BY tr.create_at DESC;";

    String getTransmissionsToSendMail = "SELECT tr.id,"
            + "tr.receiver,\n"
            + "no.sender,\n"
            + "ch.type,\n"
            + "noc.title,\n"
            + "noc.sub_title\n"
            + "FROM transmission tr\n"
            + "INNER JOIN channel ch\n"
            + "ON tr.channel_id = ch.id\n"
            + "INNER JOIN notification no\n"
            + "ON tr.notification_id = no.id\n"
            + "INNER JOIN notification_content noc\n"
            + "ON no.notification_content_id = noc.id\n"
            + "WHERE tr.state IN ('PENDING', 'FAILED')\n"
            + "AND (no.expect_send_time IS NULL OR now() > no.expect_send_time)\n"
            + "AND tr.resend_count <= :resendCount\n"
            + "AND ch.type IN ('EMAIL', 'REST')\n"
            + "AND tr.status = 1\n"
            + "AND no.status = 1\n"
            + "AND ch.status = 1;";

    String updateTransmissionState = "UPDATE transmission\n"
            + "SET state = 'NEW',\n"
            + "    update_at = now(),\n"
            + "    update_by = 'system'\n"
            + "WHERE id IN (:transmissionIds);";

    String updateTransmissionStateAndResendCount = "UPDATE transmission\n"
            + "SET state        = 'FAILED',\n"
            + "    resend_count = resend_count + 1,\n"
            + "    update_at    = now(),\n"
            + "    update_by    = 'system'\n"
            + "WHERE id IN (:transmissionIds);";

    String getTransmissionByNotificationContentId = "SELECT tr.id FROM transmission tr \n"
            + "INNER JOIN notification n ON tr.notification_id = n.id \n"
            + "INNER JOIN notification_content nc ON n.notification_content_id = nc.id \n"
            + "WHERE nc.id = :notificationContentId\n"
            + "AND tr.status =1\n"
            + "AND n.status =1\n"
            + "AND nc.status =1\n"
            + "AND tr.receiver = :receiver;";

    String findByIdAndStatus = "SELECT * FROM transmission WHERE id =:id AND status = :status";

    String updateStateById = "UPDATE transmission SET state = :state,update_by='system' WHERE id = :id";
}

