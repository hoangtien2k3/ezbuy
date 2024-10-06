package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.SummaryReport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface SummaryReportRepo extends R2dbcRepository<SummaryReport, String> {

    @Query("select * from summary_report " +
            "where date(create_at) = :localDate and status = 1 " +
            "order by create_at desc " +
            "limit 1")
    Flux<SummaryReport> getSummaryReportId(LocalDate localDate);
}
