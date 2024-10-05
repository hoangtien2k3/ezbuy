package com.ezbuy.productmodel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "summary_report")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SummaryReport implements Persistable<String> {

  @Id
  private String id;

  private Integer loginCount;//so luong truy cap

  private Integer orderCount;//tong so don hang

  private Integer successOrderCount;//tong so don hang thanh cong

  private Integer failOrderCount;//tong so don hang khong thanh cong

  private Long feeCount;//tong so tien giao dich

  private LocalDate createAt;//ngay tao

  private Integer newServiceCount;//so san pham dang ban moi

  private Integer status;//trang thai

  @Transient
  private boolean isNew = false;

  @Transient
  @Override
  public boolean isNew() {
    return this.isNew || id == null;
  }
}
