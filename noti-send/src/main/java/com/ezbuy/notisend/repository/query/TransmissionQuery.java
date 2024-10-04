package com.ezbuy.notisend.repository.query;

public interface TransmissionQuery {
    String getTransmissionsToSendMail =
            """
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
          FOR UPDATE;
      """;

    String updateTransmissionRestState =
            """
          UPDATE transmission
          SET state = 'NEW',
              update_at = now(),
              update_by = 'system'
          WHERE id IN (:transmissionIds);
      """;

    String updateTransmissionEmailState =
            """
          UPDATE transmission
          SET state = 'SENT',
              update_at = now(),
              update_by = 'system'
          WHERE id IN (:transmissionIds);
      """;

    String updateTransmissionStateAndResendCount =
            """
          UPDATE transmission
          SET state        = 'FAILED',
              resend_count = resend_count + 1,
              update_at    = now(),
              update_by    = 'system'
          WHERE id IN (:transmissionIds);
      """;

    String updateStateById =
            """
          UPDATE transmission
          SET state = :state,
              update_by = 'system'
          WHERE id = :id;
      """;

    String findByIdAndStatus =
            """
          SELECT *
          FROM transmission
          WHERE id = :id
          AND status = :status;
      """;

    String getTransmissionByNotificationContentId =
            """
          SELECT tr.id
          FROM transmission tr
          INNER JOIN notification n ON tr.notification_id = n.id
          INNER JOIN notification_content nc ON n.notification_content_id = nc.id
          WHERE nc.id = :notificationContentId
            AND tr.status = 1
            AND n.status = 1
            AND nc.status = 1
            AND tr.receiver = :receiver;
      """;

    String changeStateTransmissionByType =
            """
          UPDATE transmission tr
          SET tr.state = :state,
              tr.update_at = now(),
              tr.update_by = 'system'
          WHERE tr.receiver = :receiver
            AND tr.notification_id = (
                SELECT notification.id
                FROM notification
                INNER JOIN notification_content c
                    ON notification.notification_content_id = c.id
                WHERE c.id = :notificationContentId
            );
      """;
}
