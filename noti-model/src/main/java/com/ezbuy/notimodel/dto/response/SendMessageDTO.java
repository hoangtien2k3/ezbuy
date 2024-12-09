package com.ezbuy.notimodel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SendMessageDTO {
    private String code;    //ma loi tra ve
    private String message; //message tra ve
    private String requestId;   //dinh danh request
    private Integer totalRecord;    //tong so ban ghi
    private List<SubMessageDTO> subMessageDTOS; //danh sach submit gui tin
}
