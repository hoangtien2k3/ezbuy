package com.ezbuy.notificationmodel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SubMessageDTO {
    private String msisdn;
    private String idMT;
    private Integer status;
}
