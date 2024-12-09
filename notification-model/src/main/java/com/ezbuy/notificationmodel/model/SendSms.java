package com.ezbuy.notificationmodel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "send_sms")
public class SendSms {
    private String id;
    private String isdn;
    private String content;
    private Long status;
    private String error;
    private LocalDateTime createDate;
    private String bpId;
    private String alias;
    private String cpCode;
    private String exchangeId;
}
