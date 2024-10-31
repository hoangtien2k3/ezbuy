package com.ezbuy.notificationservice.repository.query

interface TransmissionQuery {
    companion object {
        const val getCountNoticeDTO = """
            SELECT c.type as type, COUNT(state) AS quantity
            FROM sme_notification.transmission tr
            INNER JOIN notification n ON tr.notification_id = n.id
            INNER JOIN notification_category c on n.category_id = c.id
            INNER JOIN channel c2 on tr.channel_id = c2.id
            WHERE
            state IN ('NEW')
            AND c2.type = 'REST'
            AND tr.receiver = :receiver
            AND tr.status = 1
            AND n.status = 1
            AND c2.status = 1
            GROUP BY c.type;
        """

        const val getAllByNotificationByCategoryType = """
            SELECT nc.*, tr.state 
            FROM notification_content nc
            INNER JOIN notification n ON n.notification_content_id = nc.id
            INNER JOIN notification_category nca ON n.category_id = nca.id
            INNER JOIN transmission tr ON tr.notification_id = n.id
            INNER JOIN channel c ON tr.channel_id = c.id
            WHERE tr.receiver = (:receiver)  
            AND tr.status = 1
            AND tr.state IN ('NEW', 'UNREAD', 'READ')
            AND nc.status = 1
            AND n.status = 1
            AND nca.status = 1
            AND c.status = 1
            AND c.type = 'REST'
            AND nca.type = (:categoryType) 
            ORDER BY :sort 
            LIMIT :pageSize 
            OFFSET :index;
        """

        const val changeStateTransmissionByType = """
            UPDATE transmission tr
            SET tr.state = :state, tr.update_at = Now(), tr.update_by = 'system'
            WHERE tr.receiver = :receiver
            AND tr.notification_id = (
                SELECT notification.id
                FROM notification
                INNER JOIN notification_content c ON notification.notification_content_id = c.id
                WHERE c.id = :notificationContentId
            );
        """

        const val insertTransmission = """
            INSERT INTO transmission (id, receiver, state, status, resend_count, create_at, create_by, update_at, update_by, channel_id, notification_id)
            VALUES (:id, :receiver, :state, :status, :resendCount, :createAt, :createBy, :updateAt, :updateBy, :channelId, :notificationId);
        """

        const val getAllNotificationContentByCreateAtAfter = """
            SELECT nc.* FROM notification_content nc
            INNER JOIN notification n ON nc.id = n.notification_content_id
            INNER JOIN transmission tr ON tr.notification_id = n.id
            INNER JOIN channel ch ON ch.id = tr.channel_id
            WHERE tr.receiver = :receiver
            AND tr.create_at > :newestNotiTime
            AND tr.status = 1
            AND tr.state = 'NEW'
            AND nc.status = 1 
            AND n.status = 1
            AND ch.status = 1 
            AND ch.type = 'REST'
            ORDER BY tr.create_at DESC;
        """

        const val getTransmissionsToSendMail = """
            SELECT tr.id, tr.receiver, no.sender, ch.type, noc.title, noc.sub_title
            FROM transmission tr
            INNER JOIN channel ch ON tr.channel_id = ch.id
            INNER JOIN notification no ON tr.notification_id = no.id
            INNER JOIN notification_content noc ON no.notification_content_id = noc.id
            WHERE tr.state IN ('PENDING', 'FAILED')
            AND (no.expect_send_time IS NULL OR now() > no.expect_send_time)
            AND tr.resend_count <= :resendCount
            AND ch.type IN ('EMAIL', 'REST')
            AND tr.status = 1
            AND no.status = 1
            AND ch.status = 1;
        """

        const val updateTransmissionState = """
            UPDATE transmission
            SET state = 'NEW',
                update_at = now(),
                update_by = 'system'
            WHERE id IN (:transmissionIds);
        """

        const val updateTransmissionStateAndResendCount = """
            UPDATE transmission
            SET state = 'FAILED',
                resend_count = resend_count + 1,
                update_at = now(),
                update_by = 'system'
            WHERE id IN (:transmissionIds);
        """

        const val getTransmissionByNotificationContentId = """
            SELECT tr.id FROM transmission tr 
            INNER JOIN notification n ON tr.notification_id = n.id 
            INNER JOIN notification_content nc ON n.notification_content_id = nc.id 
            WHERE nc.id = :notificationContentId
            AND tr.status = 1
            AND n.status = 1
            AND nc.status = 1
            AND tr.receiver = :receiver;
        """

        const val findByIdAndStatus = """
            SELECT * FROM transmission WHERE id = :id AND status = :status;
        """

        const val updateStateById = """
            UPDATE transmission SET state = :state, update_by = 'system' WHERE id = :id;
        """
    }
}
