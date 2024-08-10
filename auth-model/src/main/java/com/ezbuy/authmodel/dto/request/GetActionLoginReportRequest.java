package com.ezbuy.authmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetActionLoginReportRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateReport; // ngay can lay report
}
