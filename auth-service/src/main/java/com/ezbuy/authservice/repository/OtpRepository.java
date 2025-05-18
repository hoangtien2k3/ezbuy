package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.UserOtp;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface OtpRepository extends R2dbcRepository<UserOtp, String> {
    @Query(value = "select \n" +
            "  * \n" +
            "from \n" +
            "  user_otp uo \n" +
            "where \n" +
            "  uo.email = : email \n" +
            "  and uo.type = : type \n" +
            "  and uo.status = : status\n")
    Mono<UserOtp> findForgotPasswordOtp(String email, String type, Integer status);

    @Query(value = "update \n" +
            "  user_otp \n" +
            "set \n" +
            "  status = 0, \n" +
            "  update_at = now(), \n" +
            "  update_by = : updateBy \n" +
            "where \n" +
            "  email = : email \n" +
            "  and type = : type\n")
    Mono<UserOtp> disableOtp(String email, String type, String updateBy);

    @Query(value = "select now()")
    Mono<LocalDateTime> currentTimeDB();

    @Query(value = "select " +
            "  exists( " +
            "    select 1 " +
            "    from " +
            "      user_otp o " +
            "    where " +
            "      o.email = : email " +
            "      and o.type = : type " +
            "      and o.otp = : otp " +
            "      and o.exp_time >= now() " +
            "      and o.status = : status " +
            "  ) ")
    Mono<Boolean> confirmOtp(String email, String type, String otp, Integer status);
}
