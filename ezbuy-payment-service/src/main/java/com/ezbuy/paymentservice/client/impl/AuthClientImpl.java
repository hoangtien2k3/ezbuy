package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.paymentservice.client.AuthClient;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@DependsOn("webClientFactory")
public class AuthClientImpl implements AuthClient {

}
