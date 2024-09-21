package com.ezbuy.ratingmodel.dto;

import lombok.Data;

@Data
public class SearchRatingRequest {
    private String serviceAlias;
    private Float ratingFind;
    private String sort;
    private Integer pageIndex;
    private Integer pageSize;
}
