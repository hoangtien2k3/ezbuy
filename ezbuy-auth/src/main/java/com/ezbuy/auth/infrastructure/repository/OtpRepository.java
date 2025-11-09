package com.ezbuy.auth.infrastructure.repository;

import com.ezbuy.auth.domain.model.entity.UserOtpEntity;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface OtpRepository extends R2dbcRepository<UserOtpEntity, String> {
    @Query("""
            SELECT *
            FROM user_otp uo
            WHERE uo.email = :email
              AND uo.type = :type
              AND uo.status = :status
            """)
    Mono<UserOtpEntity> findForgotPasswordOtp(String email, String type, Integer status);

    @Query("""
            UPDATE user_otp
            SET status = 0,
                update_at = NOW(),
                update_by = :updateBy
            WHERE email = :email
              AND type = :type
            """)
    Mono<UserOtpEntity> disableOtp(String email, String type, String updateBy);

    @Query("""
            SELECT NOW()
            """)
    Mono<LocalDateTime> currentTimeDB();

    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM user_otp o
                WHERE o.email = :email
                  AND o.type = :type
                  AND o.otp = :otp
                  AND o.exp_time >= NOW()
                  AND o.status = :status
            )
            """)
    Mono<Boolean> confirmOtp(String email, String type, String otp, Integer status);
}
