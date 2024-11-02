package com.ezbuy.searchmodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponseDTO {
    private Integer took;

    @JsonProperty("time_out")
    private Boolean timeOut;

    @JsonProperty("hits")
    private HitsDTO hitsDTO;
}
