/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.notisend.repository.query;

public interface TransmissionQuery {
    String getTransmissionsToSendMail = "SELECT tr.id," + "tr.receiver,\n" + "tr.email,\n" + "no.sender,\n"
            + "ch.type,\n" + "noc.title,\n" + "noc.sub_title,\n" + "noc.template_mail,\n" + "noc.external_data\n"
            + "FROM transmission tr\n" + "INNER JOIN channel ch\n" + "ON tr.channel_id = ch.id\n"
            + "INNER JOIN notification no\n" + "ON tr.notification_id = no.id\n"
            + "INNER JOIN notification_content noc\n" + "ON no.notification_content_id = noc.id\n"
            + "WHERE tr.state IN ('PENDING', 'FAILED')\n"
            + "AND (no.expect_send_time IS NULL OR now() > no.expect_send_time)\n"
            + "AND tr.resend_count <= :resendCount\n" + "AND ch.type IN ('EMAIL', 'REST')\n" + "AND tr.status = 1\n"
            + "AND no.status = 1\n" + "AND ch.status = 1\n" + "LIMIT :limit \n " + "FOR UPDATE;";

    String updateTransmissionRestState = "UPDATE transmission\n" + "SET state = 'NEW',\n" + "    update_at = now(),\n"
            + "    update_by = 'system'\n" + "WHERE id IN (:transmissionIds);";

    String updateTransmissionEmailState = "UPDATE transmission\n" + "SET state = 'SENT',\n" + "    update_at = now(),\n"
            + "    update_by = 'system'\n" + "WHERE id IN (:transmissionIds);";

    String updateTransmissionStateAndResendCount = "UPDATE transmission\n" + "SET state        = 'FAILED',\n"
            + "    resend_count = resend_count + 1,\n" + "    update_at    = now(),\n" + "    update_by    = 'system'\n"
            + "WHERE id IN (:transmissionIds);";

    String updateStateById = "UPDATE transmission SET state = :state,update_by='system' WHERE id = :id";

    String findByIdAndStatus = "SELECT * FROM transmission WHERE id =:id AND status = :status";

    String getTransmissionByNotificationContentId = "SELECT tr.id FROM transmission tr \n"
            + "INNER JOIN notification n ON tr.notification_id = n.id \n"
            + "INNER JOIN notification_content nc ON n.notification_content_id = nc.id \n"
            + "WHERE nc.id = :notificationContentId\n" + "AND tr.status =1\n" + "AND n.status =1\n"
            + "AND nc.status =1\n" + "AND tr.receiver = :receiver;";

    String changeStateTransmissionByType = "UPDATE transmission tr\n"
            + "SET tr.state = :state , tr.update_at= Now(), tr.update_by='system'\n" + "WHERE tr.receiver = :receiver\n"
            + "AND tr.notification_id = (SELECT notification.id\n" + "from notification\n"
            + "INNER JOIN notification_content c\n" + "on notification.notification_content_id = c.id\n"
            + "Where c.id = :notificationContentId\n" + ")\n";
}
