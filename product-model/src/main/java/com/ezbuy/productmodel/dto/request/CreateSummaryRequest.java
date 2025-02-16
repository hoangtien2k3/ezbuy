package com.ezbuy.productmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSummaryRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateReport; // ngay lay bao cao
}
