package com.ezbuy.triggerservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SyncOrderStateRequest {
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @JsonIgnore
    private Integer limit;

    @JsonIgnore
    private int runCount = 100;

    @JsonIgnore
    private LocalDateTime startDate;

    @JsonIgnore
    private LocalDateTime endDate;
}
