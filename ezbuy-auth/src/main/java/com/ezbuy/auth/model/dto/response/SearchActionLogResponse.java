package com.ezbuy.auth.model.dto.response;

import com.ezbuy.auth.model.dto.PaginationDTO;

import java.util.List;

import com.ezbuy.auth.model.entity.ActionLogEntity;
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
