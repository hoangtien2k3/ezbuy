package com.ezbuy.ratingmodel.dto;

import java.util.List;
import lombok.Data;

@Data
public class RatingServiceResponse {
    private Float rating;
    private Float maxRating;
    private Long numberRate;
    private String serviceAlias;
    private List<RatingDetailDTO> listRatingDetail;
    private List<RatingCommentDTO> listRatingComment;
    private PaginationDTO pagination;
}
