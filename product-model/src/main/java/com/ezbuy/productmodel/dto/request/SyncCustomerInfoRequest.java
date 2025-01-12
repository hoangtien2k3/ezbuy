package com.ezbuy.productmodel.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SyncCustomerInfoRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String state;
    private Integer maxRetry;

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
