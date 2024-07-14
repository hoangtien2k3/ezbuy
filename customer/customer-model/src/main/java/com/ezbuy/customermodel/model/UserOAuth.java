package com.ezbuy.customermodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("user_oauth")
public class UserOAuth {
    @Id
    private Long id; // id
    private Integer shopUserId; // id shop user
    private String identifier; // xac thuc
    private String accessToken; // token truy cap
    private String refreshToken; // token tai lai
}