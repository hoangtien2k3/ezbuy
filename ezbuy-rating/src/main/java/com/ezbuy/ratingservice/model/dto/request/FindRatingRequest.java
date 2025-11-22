package com.ezbuy.ratingservice.model.dto.request;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindRatingRequest extends PageRequest {
    private String ratingTypeCode;
    private String targetId;
    private String username;
    private String custName;
    private String content;
    private Integer rating;
    private Integer hasImage;
    private Integer hasVideo;
    private Integer ratingStatus;
    private String state;
    private Integer displayStatus;
    private Integer sumRateStatus;
    private String targetUser;
    private BigInteger fromDate;
    private BigInteger toDate;
}
