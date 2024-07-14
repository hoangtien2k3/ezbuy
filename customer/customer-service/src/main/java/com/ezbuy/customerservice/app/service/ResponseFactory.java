package com.ezbuy.customerservice.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseFactory {
    public <T> T createResponse(T response) {
        return response;
    }
}
