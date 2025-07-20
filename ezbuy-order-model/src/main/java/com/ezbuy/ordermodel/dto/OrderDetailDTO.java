package com.ezbuy.ordermodel.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {

    private String id; // ma dinh danh don hang

    private String orderCode; // ma don hang order

    private Long totalFee; // tong tien

    private String currency; // don vi tien

    private Integer state; // trang thai don hang 0: Chua trien khai, 1: Da trien khai, 4 Da huy

    private Object createAt; // ngay tao

    private Boolean isPreOrder; // flag don tu van, true: don tu van, false: khong phai don tu van

    private List<OrderItemDTO> itemList; // danh sach san pham dich vu cua don hang

    public void addItem(List<OrderItemDTO> items) {
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        if (items != null) {
            itemList.addAll(items);
        }
    }
}
