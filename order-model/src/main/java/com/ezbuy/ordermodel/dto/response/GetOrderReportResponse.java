package com.ezbuy.ordermodel.dto.response;

import lombok.Data;

@Data
public class GetOrderReportResponse {

    private Integer orderCount; // tong so don hang trong 1 ngay
    private Integer successOrderCount; // tong so don hang thanh cong trong 1 ngay
    private Integer failOrderCount; // tong so don hang that bai trong 1 ngay
    private Long feeCount; // tong so tien cua tat ca don hang trong 1 ngay
}
