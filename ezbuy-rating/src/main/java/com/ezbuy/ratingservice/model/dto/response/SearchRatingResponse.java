package com.ezbuy.ratingservice.model.dto.response;

import com.ezbuy.ratingservice.model.dto.PaginationDTO;
import com.ezbuy.ratingservice.model.dto.RatingDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchRatingResponse {
    private List<RatingDTO> lstRating;
    private PaginationDTO pagination;
}
