package com.ezbuy.auth.application.dto.response;

import com.ezbuy.auth.application.dto.PaginationDTO;

import java.util.List;

import com.ezbuy.auth.domain.model.entity.ActionLogEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchActionLogResponse {
    private List<ActionLogEntity> actionLogs;
    private PaginationDTO pagination;
}
