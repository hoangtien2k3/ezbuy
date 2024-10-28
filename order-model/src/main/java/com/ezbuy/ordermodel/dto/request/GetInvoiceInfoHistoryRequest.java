package com.ezbuy.ordermodel.dto.request;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class GetInvoiceInfoHistoryRequest {
    private String userId; // id user dang nhap
    private String organizationId; // id doanh nghiep

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate; // Tu ngay

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate; // Den ngay
}
