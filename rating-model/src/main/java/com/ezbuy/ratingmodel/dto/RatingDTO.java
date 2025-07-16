package com.ezbuy.ratingmodel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO {
    private String id;
    private String ratingTypeCode;
    private String targetId;
    private String username;
    private String custName;
    private Float rating;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime ratingDate;

    private Integer hasImage;
    private Integer hasVideo;
    private Integer status;
    private String state;
    private Integer displayStatus;
    private Integer sumRateStatus;
    private String targetUser;
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
    private String service_alias;
}
