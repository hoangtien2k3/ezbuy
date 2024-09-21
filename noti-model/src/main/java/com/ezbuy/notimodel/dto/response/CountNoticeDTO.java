package com.ezbuy.notimodel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountNoticeDTO {
    private String type;
    private Integer quantity;
}
