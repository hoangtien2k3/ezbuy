package com.ezbuy.notificationmodel.dto.response;

import java.util.List;

public record CountNoticeResponseDTO(Integer total, List<CountNoticeDTO> detail) {}
