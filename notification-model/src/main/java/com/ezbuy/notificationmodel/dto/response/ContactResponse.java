package com.ezbuy.notificationmodel.dto.response;

import com.ezbuy.notificationmodel.dto.ContactInfoDTO;
import java.util.List;
import lombok.Data;

@Data
public class ContactResponse {
    private String errorCode;
    private String message;
    private List<ContactInfoDTO> data;
}
