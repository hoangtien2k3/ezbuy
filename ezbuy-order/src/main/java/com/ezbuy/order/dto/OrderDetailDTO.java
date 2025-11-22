package com.ezbuy.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {

    private String id;
    private String orderCode;
    private Long totalFee;
    private String currency;
    private Integer state;
    private Object createAt;
    private Boolean isPreOrder;
    private List<OrderItemDTO> itemList;

    public void addItem(List<OrderItemDTO> items) {
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        if (items != null) {
            itemList.addAll(items);
        }
    }
}
