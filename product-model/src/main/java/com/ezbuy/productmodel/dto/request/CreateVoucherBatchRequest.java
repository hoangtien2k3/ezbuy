package com.ezbuy.productmodel.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVoucherBatchRequest extends ServiceGroupRequest {

  private String id; //id lo voucher
  private String code; // ma lo voucher
  private String description; // mo ta lo voucher
  private String voucherTypeId; //Id loai lo voucher
  private Integer quantity; // so luong voucher
  private Integer expiredPeriod; // thoi gian het han
  private String state; // trang thai
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime expiredDate; // ngay het han
  private LocalDateTime createAt;
  private String createBy;
  private LocalDateTime updateAt;
  private String updateBy;
}
