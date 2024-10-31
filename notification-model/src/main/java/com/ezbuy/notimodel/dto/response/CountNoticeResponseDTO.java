package com.ezbuy.notimodel.dto.response;

import java.util.List;

public record CountNoticeResponseDTO(
        Integer total,
        List<CountNoticeDTO> detail
) {
}
