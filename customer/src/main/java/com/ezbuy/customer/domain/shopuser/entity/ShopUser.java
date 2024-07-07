package com.ezbuy.customer.domain.shopuser.entity;

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
    private Integer id;
    private Integer customerId;
    private String username;
    private String usernameCanonical;
    private Short enabled;
    private String salt;
    private String password;
    private LocalDateTime lastLogin;
    private String passwordResetToken;
    private LocalDateTime passwordRequestedAt;
    private String emailVerificationToken;
    private LocalDateTime verifiedAt;
    private Short locked;
    private LocalDateTime expiresAt;
    private LocalDateTime credentialsExpireAt;
    private String roles;
    private String email;
    private String emailCanonical;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}