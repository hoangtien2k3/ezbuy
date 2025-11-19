package com.ezbuy.order.dto;

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
    private String telecomServiceId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String telecomServiceName;
    private String name;
    private String description;
    private Long price;
    private Long originPrice;
    private String currency;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer quantity;
    private String duration;
    private String telecomServiceAlias;
}
