package com.ezbuy.ordermodel.dto.response;

import java.util.List;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@XmlRootElement(name = "return")
public class GetOrderHistoryResponse {

    @XmlElement(name = "order")
    private OrderDTO order; // thong tin don hang

    @XmlElement(name = "orderItem")
    private List<OrderItemDTO> orderItem; // thong tin san pham don hang

    @Data
    public static class OrderDTO {
        private String orderCode; // ma don hang
        private String createDate; // ngay tao
        private Integer state; // trang thai don hang (chua hoan thanh, da hoan thanh, da huy)
        private Boolean isPreOrder; // true: don tu van, false: khong phai don tu van
    }

    @Data
    public static class OrderItemDTO {
        private Long duration; // thoi han dich vu
        private String name; // ten san pham
        private Long originPrice; // don gia
        private Long price; // gia sau chiet khau
        private Integer quantity; // so luong
        private Integer state; // trang thai san pham
        private String telecomServiceId; // ma dich vu
        private Boolean durationAsMonth; // true: thoi han tinh theo thang, false: thoi han tinh theo ngay
    }
}
