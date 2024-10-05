package com.ezbuy.orderservice.client.impl;

import com.ezbuy.common.ObjectClientChannel;
import com.ezbuy.common.ViettelService;
import com.ezbuy.orderservice.client.ProvisioningClient;
import com.ezbuy.sme.framework.factory.MarshallerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class ProvisioningClientImpl implements ProvisioningClient {
    private static final Logger logger = LogManager.getLogger(MarshallerFactory.class);

    @Value("${client.pro.host}")
    private String host;
    @Value("${client.pro.port}")
    private int port;
    @Value("${client.pro.username}")
    private String userName;
    @Value("${client.pro.password}")
    private String password;
    @Value("${client.pro.async}")
    private boolean async;

    @Override
    public Mono<ViettelService> callProvisioning(ViettelService dataConnect) {
        ObjectClientChannel objectClientChannel = new ObjectClientChannel(host, port, userName, password, async);
        try {
            var a = (ViettelService) objectClientChannel.send(dataConnect);
            return  Mono.just (a );
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return Mono.just(null);
        }
    }
}
