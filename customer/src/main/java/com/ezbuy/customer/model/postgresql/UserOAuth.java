package com.ezbuy.customer.model.postgresql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("user_oauth")
public class UserOAuth {
    @Id
    private Long id; // id

    @Column("shop_user_id")
    private Integer shopUserId; // id shop user

    private String identifier; // xac thuc

    @Column("access_token")
    private String accessToken; // token truy cap

    @Column("refresh_token")
    private String refreshToken; // token tai lai
}
