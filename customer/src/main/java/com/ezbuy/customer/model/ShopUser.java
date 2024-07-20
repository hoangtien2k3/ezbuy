package com.ezbuy.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
import lombok.Data;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("shop_user")
public class ShopUser {
    @Id
    private Long id; // id
    private Long customerId; // id khach hang
    private Integer statusView; // trang thai xem
    private Integer statusAccount; // trang thai tai khoan
    private LocalDateTime lastLogin; // thoi gian dang nhap cuoi cung
    private LocalDateTime expiresAt; // thoi gian het han
    private String roles; // vai tro
    private String securityQuestion; // cau hoi bao mat
    private String securityAnswer; // cau tra loi bao mat
    private LocalDateTime createdAt; // thoi gian tao
    private LocalDateTime updatedAt; // thoi gian cap nhat
    private Integer loyaltyPoints; // diem tich luy
}