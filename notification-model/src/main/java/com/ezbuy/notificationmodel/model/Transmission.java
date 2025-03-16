package com.ezbuy.notificationmodel.model;

import com.ezbuy.notificationmodel.model.base.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@ToString
@Table(name = "transmission")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transmission extends EntityBase {

    @Id
    private String id;

    private String notificationId;
    private String receiver;
    private String email;
    private String channelId;
    private String state;
    private Integer status;
    private int resendCount;

    public void inreaseResendCount() {
        resendCount++;
    }
}
