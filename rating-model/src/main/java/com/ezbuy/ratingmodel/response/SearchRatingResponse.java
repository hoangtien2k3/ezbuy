package com.ezbuy.ratingmodel.response;

import com.ezbuy.ratingmodel.dto.PaginationDTO;
import com.ezbuy.ratingmodel.dto.RatingDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchRatingResponse {
    private List<RatingDTO> lstRating; //danh sach danh gia
    private PaginationDTO pagination; //thong tin phan trang
}
