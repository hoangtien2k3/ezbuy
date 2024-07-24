package com.ezbuy.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
public class HealthCheckController {
    @GetMapping
    public Mono<Boolean> healthCheck() {
        return Mono.just(Boolean.TRUE);
    }
}
