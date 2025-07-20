package com.ezbuy.searchmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTORequest {
    private String type;
    private String keyword;
    private Integer from;
    private Integer size;
}
