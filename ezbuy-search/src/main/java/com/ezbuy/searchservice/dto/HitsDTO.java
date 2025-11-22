package com.ezbuy.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HitsDTO {
    @JsonProperty("max_score")
    private Float maxScore;

    private List<HitDTO> hits;
}
