package com.ezbuy.noti.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountNoticeDTO {
    private String type;
    private Integer quantity;
}
