package com.ezbuy.customerservice.infrastructure.config.jwt;

import com.ezbuy.customermodel.constants.Const;
import com.ezbuy.customerservice.domain.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class UserDetailsConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return (email) -> customerRepository.findByEmail(email)
                .map(cus -> User.withUsername(cus.getUsername())
                        .password(cus.getPassword())
                        .roles(Const.ROLE.USER)
                        .disabled(true)
                        .build());
    }

}
