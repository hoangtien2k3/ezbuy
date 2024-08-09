package com.ezbuy.sendnotification.model.noti;

import lombok.Data;

import java.util.List;

@Data
public class ContactResponse {
    private String errorCode;
    private String message;
    private List<ContactInfoDTO> data;
}
