package com.ezbuy.ratingmodel.dto;

import lombok.Data;

import java.util.List;

@Data
public class RatingServiceResponse {
    private Float rating;
    private Float maxRating;
    private Long numberRate;
    private String serviceAlias;
    private List<RatingDetailDTO> listRatingDetail;
    private List<RatingCommentDTO> listRatingComment;
    private PaginationDTO pagination; //thong tin phan trang
}
