package com.ezbuy.customer.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    String generateToken(UserDetails userDetails);
}
