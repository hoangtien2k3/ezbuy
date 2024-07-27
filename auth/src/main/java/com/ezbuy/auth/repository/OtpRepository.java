package com.ezbuy.auth.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ezbuy.auth.model.postgresql.UserOtp;

import reactor.core.publisher.Mono;

public interface OtpRepository extends R2dbcRepository<UserOtp, UUID> {

    @Query(value = "select * from user_otp uo where uo.email =:email and uo.type =:type and uo.status =:status")
    Mono<UserOtp> findForgotPasswordOtp(String email, String type, Integer status);

    @Query(value = "UPDATE user_otp t SET t.status = 0, t.update_at = now(), t.update_by = :updateBy WHERE t.email =:email and t.type =:type")
    Mono<UserOtp> disableOtp(String email, String type, String updateBy);

    @Query(value = "Select now() from dual")
    Mono<LocalDateTime> currentTimeDb();

    @Query(
            value =
                    "select exists(select o.id from user_otp o where o.email =:email and o.type =:type and o.otp=:otp and o.exp_time >= now() and o.status =:status LIMIT 1)")
    Mono<Boolean> confirmOtp(String email, String type, String otp, Integer status);
}
