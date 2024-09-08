/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.UserOtp;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface OtpRepository extends R2dbcRepository<UserOtp, String> {

    @Query(value = "select * from user_otp uo where uo.email = :email and uo.type = :type and uo.status = :status")
    Mono<UserOtp> findForgotPasswordOtp(String email, String type, Integer status);

    @Query(
            value =
                    "update user_otp set status = 0, update_at = now(), update_by = :updateBy where email = :email and type = :type")
    Mono<UserOtp> disableOtp(String email, String type, String updateBy);

    @Query(value = "select now()")
    Mono<LocalDateTime> currentTimeDB();

    @Query(
            value =
                    "select exists(select 1 from user_otp o where o.email = :email and o.type = :type and o.otp = :otp and o.exp_time >= now() and o.status = :status)")
    Mono<Boolean> confirmOtp(String email, String type, String otp, Integer status);
}
