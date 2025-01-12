package com.ezbuy.productmodel.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListAreaInsResponse {
    private String errorCode;
    private String description;
    private List<ListAreaInsDataDTO> data;
}
