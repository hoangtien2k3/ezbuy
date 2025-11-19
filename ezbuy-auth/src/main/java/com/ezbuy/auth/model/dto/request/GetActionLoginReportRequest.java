package com.ezbuy.auth.model.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetActionLoginReportRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateReport;
}
