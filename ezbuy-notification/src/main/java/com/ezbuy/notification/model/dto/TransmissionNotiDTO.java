package com.ezbuy.notification.model.dto;

import lombok.Data;

@Data
public class TransmissionNotiDTO {
    private String id;
    private String receiver;
    private String email;
    private String sender;
    private String type;
    private String title;
    private String subTitle;
    private String templateMail;
    private String userName;
    private String externalData;
}
