package com.ezbuy.framework.filter.properties;

import java.util.List;

import org.springframework.http.HttpMethod;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetryProperties {
    private boolean isEnable = true;
    private int count = 2;
    private List<HttpMethod> methods = List.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE);
    private List<? extends Class<? extends Exception>> exceptions =
            List.of(ConnectTimeoutException.class, ReadTimeoutException.class);
}
