package com.ezbuy.customer.domain.customer.repository;

import com.ezbuy.customer.domain.shopuser.entity.ShopUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserOAuthRepository extends R2dbcRepository<ShopUser, Integer> {

}
