package com.ezbuy.notisend.model.dto.response;

import com.ezbuy.notisend.model.dto.ContactInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ContactResponse {
    private String errorCode;
    private String message;
    private List<ContactInfoDTO> data;
}
