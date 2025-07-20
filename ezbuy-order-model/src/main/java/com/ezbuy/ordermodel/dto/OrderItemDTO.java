package com.ezbuy.ordermodel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private String id;

    private String orderId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String telecomServiceId; // ma dich vu

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String telecomServiceName; // ten dich vu

    private String name; // ten sam pham dich vu

    private String description; // mo ta

    private Long price; // gia sau chiet khau

    private Long originPrice; // gia goc

    private String currency; // don vi tien

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer quantity; // so luong

    private String duration; // thoi han dich vu

    private String telecomServiceAlias; // alias dich vu
}
