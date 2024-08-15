package com.ezbuy.ratingmodel.response;

import com.ezbuy.ratingmodel.dto.PaginationDTO;
import com.ezbuy.ratingmodel.dto.RatingDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchServiceRatingResponse {
    private List<RatingDTO> lstServiceRatingDTO; //danh sach nhom dich vu
    private PaginationDTO pagination; //thong tin phan trang
}
