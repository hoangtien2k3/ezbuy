package com.ezbuy.customerservice.domain.shopuser.repository;

import com.ezbuy.customermodel.model.ShopUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ShopUserRepository extends R2dbcRepository<ShopUser, Long> {

}
