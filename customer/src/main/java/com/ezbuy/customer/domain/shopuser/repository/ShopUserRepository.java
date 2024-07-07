package com.ezbuy.customer.domain.shopuser.repository;

import com.ezbuy.customer.domain.shopuser.entity.ShopUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ShopUserRepository extends R2dbcRepository<ShopUser, Integer> {
}
