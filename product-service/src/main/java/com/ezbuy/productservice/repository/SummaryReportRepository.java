package com.ezbuy.productservice.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ezbuy.productmodel.model.SummaryReport;
import com.ezbuy.productmodel.request.QueryReport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface SummaryReportRepository extends R2dbcRepository<SummaryReport, UUID> {

  @Query("select ifnull(sum(new_service_count), 0)   new_products,"
      + "       ifnull(sum(login_count), 0)         access,"
      + "       ifnull(sum(order_count), 0)         total_orders,"
      + "       ifnull(sum(success_order_count), 0) successful_orders,"
      + "       ifnull(sum(fail_order_count), 0)    failed_orders,"
      + "       ifnull(sum(fee_count), 0)           transaction_value "
      + " from sme_product.summary_report "
      + " where create_at >= :fromDate "
      + "  and create_at < :toDate")
  Mono<QueryReport> getSummaryReport(LocalDateTime fromDate, LocalDateTime toDate);
}
