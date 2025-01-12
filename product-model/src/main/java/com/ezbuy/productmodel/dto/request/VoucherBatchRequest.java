package com.ezbuy.productmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherBatchRequest extends ServiceGroupRequest {
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate fromDate; // den ngay
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate toDate; // tu ngay
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate fromExpiredDate; // ngay het han
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate toExpiredDate; // den ngay het han
  private Integer expiredPeriod; // thoi gian het han
  private String id;
  private String code; // ma lo voucher
  private String voucherType; // voucher type
  private String state; // trang thai
  private String description; // mo ta
  private Integer quantity; // so luong voucher trong 1 lo
}

