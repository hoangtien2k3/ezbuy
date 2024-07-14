package com.ezbuy.customermodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
    private Long customerGroupId; // id group
    private String email; // email
    private String username; // ten dang nhap
    private String password; // mat khau
    private String firstName; // mat khau
    private String lastName; // mat khau
    private String displayName; // ten hien thi
    private LocalDateTime birthday; // ngay sinh
    private String imageUrl; // link anh
    private String phoneNumber; // so dien thoai
    private String gender; // gioi tinh
    private LocalDateTime createdAt; // thoi gian tao
    private LocalDateTime updatedAt; // thoi gian cap nhat
    private Integer isVerifiedEmail; // xac thuc email
    private Boolean subscribedToNewsletter; // dang ky nhan thong bao
}