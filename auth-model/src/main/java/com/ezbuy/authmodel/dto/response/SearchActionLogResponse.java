package com.ezbuy.authmodel.dto.response;

import com.ezbuy.authmodel.dto.PaginationDTO;
import com.ezbuy.authmodel.model.ActionLog;
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
