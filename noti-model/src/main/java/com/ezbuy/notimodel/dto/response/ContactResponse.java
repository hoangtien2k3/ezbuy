package com.ezbuy.notimodel.dto.response;

import com.ezbuy.notimodel.dto.ContactInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ContactResponse {

    private String errorCode;
    private String message;
    private List<ContactInfoDTO> data;

}
