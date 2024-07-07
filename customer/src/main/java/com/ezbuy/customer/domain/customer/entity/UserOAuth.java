package com.ezbuy.customer.domain.customer.entity;

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
    private Integer id;
    private Integer userId;
    private String provider;
    private String identifier;
    private String accessToken;
    private String refreshToken;
}