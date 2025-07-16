package com.ezbuy.productmodel.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherBatchRequest extends ServiceGroupRequest {
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromExpiredDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toExpiredDate;

    private Integer expiredPeriod;
    private String id;
    private String code;
    private String voucherType;
    private String state;
    private String description;
    private Integer quantity;
}
