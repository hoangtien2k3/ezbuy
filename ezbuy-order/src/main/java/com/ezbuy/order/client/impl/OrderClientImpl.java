package com.ezbuy.order.client.impl;

import com.ezbuy.order.client.OrderClient;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@DependsOn("webClientFactory")
public class OrderClientImpl implements OrderClient {

}
