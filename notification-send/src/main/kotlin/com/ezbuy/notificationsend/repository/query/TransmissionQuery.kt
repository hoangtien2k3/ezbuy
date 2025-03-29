package com.ezbuy.notificationsend.repository.query

interface TransmissionQuery {
    companion object {
        const val GET_TRANSMISSIONS_TO_SEND_MAIL = """
          SELECT tr.id, tr.receiver, tr.email, no.sender, ch.type, noc.title, noc.sub_title, noc.template_mail, noc.external_data
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
          AND ch.status = 1
          LIMIT :limit
          FOR UPDATE SKIP LOCKED;
        """

        const val UPDATE_TRANSMISSION_REST_STATE = """
          UPDATE transmission
          SET state = 'NEW',
              update_at = now(),
              update_by = 'system'
          WHERE id = ANY (:transmissionIds);
        """

        const val UPDATE_TRANSMISSION_EMAIL_STATE = """
          UPDATE transmission
          SET state = 'SENT',
              update_at = now(),
              update_by = 'system'
          WHERE id = ANY (:transmissionIds);
        """

        const val UPDATE_TRANSMISSION_STATE_AND_RESEND_COUNT = """
          UPDATE transmission
          SET state        = 'FAILED',
              resend_count = resend_count + 1,
              update_at    = now(),
              update_by    = 'system'
          WHERE id = ANY (:transmissionIds);
        """

        const val UPDATE_STATE_BY_ID = """
          UPDATE transmission
          SET state = :state,
              update_by = 'system'
          WHERE id = :id;
        """

        const val FIND_BY_ID_AND_STATUS = """
          SELECT *
          FROM transmission
          WHERE id = :id
          AND status = :status;
        """

        const val GET_TRANSMISSION_BY_NOTIFICATION_CONTENT_ID = """
          SELECT tr.id
          FROM transmission tr
          INNER JOIN notification n ON tr.notification_id = n.id
          INNER JOIN notification_content nc ON n.notification_content_id = nc.id
          WHERE nc.id = :notificationContentId
            AND tr.status = 1
            AND n.status = 1
            AND nc.status = 1
            AND tr.receiver = :receiver;
        """

        const val CHANGE_STATE_TRANSMISSION_BY_TYPE = """
          UPDATE transmission tr
          SET state     = :state,
              update_at = CURRENT_TIMESTAMP,
              update_by = 'system'
          WHERE tr.receiver = :receiver
          AND EXISTS (
            SELECT 1
            FROM notification n
            INNER JOIN notification_content c
                ON n.notification_content_id = c.id
            WHERE c.id = :notificationContentId
            AND n.id = tr.notification_id
          );
        """

    }
}
