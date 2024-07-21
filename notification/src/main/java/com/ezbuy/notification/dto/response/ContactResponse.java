package com.ezbuy.notification.dto.response;

import com.ezbuy.notification.dto.ContactInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ContactResponse {
    private String errorCode;
    private String message;
    private List<ContactInfoDTO> data;
}
