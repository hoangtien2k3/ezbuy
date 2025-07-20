package com.ezbuy.ratingmodel.response;

import com.ezbuy.ratingmodel.dto.PaginationDTO;
import com.ezbuy.ratingmodel.dto.RatingDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchRatingResponse {
    private List<RatingDTO> lstRating;
    private PaginationDTO pagination;
}
