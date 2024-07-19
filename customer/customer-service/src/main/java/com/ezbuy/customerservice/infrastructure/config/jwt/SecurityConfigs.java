package com.ezbuy.customerservice.infrastructure.config.jwt;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class SecurityConfigs {
    public static final String[] PERMITTED_URL = new String[]{
            "/api/v1/customers/create",
            "/api/v1/customers/auth",
            "/api/v1/customers/test"
    };

    private final SecurityContextRepository securityContextRepository;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
//                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .headers(headers -> headers.cache(ServerHttpSecurity.HeaderSpec.CacheSpec::disable))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(PERMITTED_URL)
                        .permitAll()
                        .anyExchange()
                        .authenticated()
                )
                .build();

    }
}
