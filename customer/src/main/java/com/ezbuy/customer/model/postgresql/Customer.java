package com.ezbuy.customer.model.postgresql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("customer")
public class Customer {
    @Id
    private Long id; // id
    @Column("customer_group_id")
    private Long customerGroupId; // id group
    private String email; // email
    private String username; // ten dang nhap
    private String password; // mat khau
    @Column("first_name")
    private String firstName; // mat khau
    @Column("last_name")
    private String lastName; // mat khau
    @Column("display_name")
    private String displayName; // ten hien thi
    private LocalDateTime birthday; // ngay sinh
    @Column("image_url")
    private String imageUrl; // link anh
    @Column("phone_number")
    private String phoneNumber; // so dien thoai
    private String gender; // gioi tinh
    @Column("created_at")
    private LocalDateTime createdAt; // thoi gian tao
    @Column("updated_at")
    private LocalDateTime updatedAt; // thoi gian cap nhat
    @Column("email_verified")
    private Integer emailVerified; // xac thuc email
    @Column("subscribed_to_newsletter")
    private Boolean subscribedToNewsletter; // dang ky nhan thong bao
}