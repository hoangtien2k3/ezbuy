package com.ezbuy.customer.model.postgresql;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("shop_user")
public class ShopUser {
    @Id
    private Long id; // id

    @Column("customer_id")
    private Long customerId; // id khach hang

    @Column("status_view")
    private Integer statusView; // trang thai xem

    @Column("status_account")
    private Integer statusAccount; // trang thai tai khoan

    @Column("last_login")
    private LocalDateTime lastLogin; // thoi gian dang nhap cuoi cung

    @Column("expired_at")
    private LocalDateTime expiresAt; // thoi gian het han

    private String roles; // vai tro

    @Column("security_question")
    private String securityQuestion; // cau hoi bao mat

    @Column("security_answer")
    private String securityAnswer; // cau tra loi bao mat

    @Column("created_at")
    private LocalDateTime createdAt; // thoi gian tao

    @Column("updated_at")
    private LocalDateTime updatedAt; // thoi gian cap nhat

    @Column("loyalty_points")
    private Integer loyaltyPoints; // diem tich luy
}
