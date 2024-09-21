package com.ezbuy.ratingmodel.dto;

import lombok.Data;

@Data
public class RatingCommentDTO {
    private Float rating;
    private String ratingDate;
    private String custName;
    private String content;
}
