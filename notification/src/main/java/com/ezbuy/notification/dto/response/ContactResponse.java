package com.ezbuy.notification.dto.response;

import java.util.List;

import com.ezbuy.notification.dto.ContactInfoDTO;

import lombok.Data;

@Data
public class ContactResponse {
    private String errorCode;
    private String message;
    private List<ContactInfoDTO> data;
}
