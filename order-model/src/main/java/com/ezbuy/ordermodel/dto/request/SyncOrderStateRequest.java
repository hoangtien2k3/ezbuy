package com.ezbuy.ordermodel.dto.request;

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

    public void reduceCount() {
        this.runCount = this.runCount - 1;
    }

    public long getOffSet() {
        if (limit == null) {
            limit = 0;
        }
        return (long) (100 - runCount) * limit;
    }
}
