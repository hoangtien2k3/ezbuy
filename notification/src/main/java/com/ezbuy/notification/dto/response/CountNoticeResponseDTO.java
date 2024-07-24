package com.ezbuy.notification.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountNoticeResponseDTO {
    private Integer total;
    private List<CountNoticeDTO> detail;
}
