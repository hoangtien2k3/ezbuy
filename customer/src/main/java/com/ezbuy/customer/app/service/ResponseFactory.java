package com.ezbuy.customer.app.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseFactory {
    public <T> T createResponse(T response) {
        return response;
    }
}
