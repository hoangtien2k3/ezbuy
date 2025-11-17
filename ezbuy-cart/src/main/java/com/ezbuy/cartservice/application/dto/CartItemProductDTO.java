package com.ezbuy.cartservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemProductDTO {
    private String itemId;
    private String cartId;
    private String productId;
    private String templateCode;
    private String templateName;
    private Long quantity;
    private String imageUrl;
    private Long totalPrice;
    private Long unitPrice;
    private Long discountPrice;
    private String durationValue;
    private LocalDateTime createAt;

    public int compareCreateAt(CartItemProductDTO cartItem) {
        if (this.createAt == null) {
            return cartItem.getCreateAt() == null ? 0 : -1;
        }
        if (cartItem.createAt == null) {
            return -1;
        }
        return this.createAt.compareTo(cartItem.createAt);
    }
}
