package com.ezbuy.notimodel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountNoticeResponseDTO {
    private Integer total;
    private List<CountNoticeDTO> detail;
}
