package com.ezbuy.searchmodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HitsDTO {
    @JsonProperty("max_score")
    private Float maxScore;

    private List<HitDTO> hits;
}
