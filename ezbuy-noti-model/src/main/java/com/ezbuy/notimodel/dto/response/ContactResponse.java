package com.ezbuy.notimodel.dto.response;

import com.ezbuy.notimodel.dto.ContactInfoDTO;
import java.util.List;
import lombok.Data;

@Data
public class ContactResponse {
    private String errorCode;
    private String message;
    private List<ContactInfoDTO> data;
}
