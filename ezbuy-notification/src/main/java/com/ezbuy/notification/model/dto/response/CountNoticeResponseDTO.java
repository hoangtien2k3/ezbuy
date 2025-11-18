package com.ezbuy.notification.model.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@Builder
public class CountNoticeResponseDTO {
    private Integer total;
    private List<CountNoticeDTO> detail;

    public CountNoticeResponseDTO(Integer total, List<CountNoticeDTO> detail) {
        this.total = total;
        this.detail = detail;
    }
}
