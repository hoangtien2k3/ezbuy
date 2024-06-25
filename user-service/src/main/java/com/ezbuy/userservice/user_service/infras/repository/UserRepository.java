package com.ezbuy.userservice.user_service.infras.repository;

import com.ezbuy.usermodel.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserRepository extends R2dbcRepository<User, Long> {

}
