package com.ezbuy.auth.model.dto.response;

import java.util.List;

import com.ezbuy.auth.model.dto.PaginationDTO;
import com.ezbuy.auth.model.postgresql.ActionLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchActionLogResponse {
    private List<ActionLog> actionLogs;
    private PaginationDTO pagination;
}
