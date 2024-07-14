package com.ezbuy.customerservice.domain.customer.repository;

import com.ezbuy.customermodel.model.ShopUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserOAuthRepository extends R2dbcRepository<ShopUser, Long> {

}
