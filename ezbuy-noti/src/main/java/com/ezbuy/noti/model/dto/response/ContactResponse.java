package com.ezbuy.noti.model.dto.response;

import com.ezbuy.noti.model.dto.ContactInfoDTO;
import java.util.List;
import lombok.Data;

@Data
public class ContactResponse {
    private String errorCode;
    private String message;
    private List<ContactInfoDTO> data;
}
