package com.ezbuy.productmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchVoucherRequest extends ServiceGroupRequest {
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate fromDate;//tu ngay
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate toDate;//den ngay
  @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime fromExpiredDate;//tu ngay het han
  @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime toExpiredDate;//den ngay het han
  private Integer expiredPeriod;//so gio het han
  private String id;//id voucher
  private String code;//ma voucher
  private String voucherTypeId;//id loai voucher
  private String batchId;//id lo voucher
  private String state;//trang thai
}
