package com.ezbuy.auth.model.dto.response;

import com.ezbuy.auth.model.dto.PaginationDTO;
import com.ezbuy.auth.model.postgresql.ActionLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchActionLogResponse {
    private List<ActionLog> actionLogs;
    private PaginationDTO pagination;
}
