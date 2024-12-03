package com.ezbuy.notificationmodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
public class UserTransmissionPageDTO {
    private List<UserTransmissionDTO> results;
    private Pagination pagination;

    @Data
    @Builder
    public static class Pagination {
        private final Integer pageIndex;
        private final Integer pageSize;
        private final Long totalRecords;

        @JsonProperty
        public boolean first() {
            return pageIndex == 0;
        }

        @JsonProperty
        public boolean last() {
            return (long) (pageIndex + 1) * pageSize >= totalRecords;
        }
    }
}
