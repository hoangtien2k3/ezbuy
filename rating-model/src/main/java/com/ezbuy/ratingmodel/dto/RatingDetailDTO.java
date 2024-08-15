package com.ezbuy.ratingmodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RatingDetailDTO {
    private Long rating; //so diem danh gia
    private Long numberRate; //so luot danh gia
}
