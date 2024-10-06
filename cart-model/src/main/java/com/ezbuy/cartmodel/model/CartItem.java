package com.ezbuy.cartmodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_item")
@Builder
public class CartItem {
    private String id;
    private String productId;
    private String telecomServiceId;
    private String serviceAlias; //Bo sung alias PYCXXX/LuongToanTrinh
    private String cartId;
    private Long quantity;
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
}
