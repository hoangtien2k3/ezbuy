package com.ezbuy.framework.filter.properties;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetryProperties {
    private boolean isEnable = true;
    private int count = 2;
    private List<HttpMethod> methods = Arrays.asList(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE);
    private List<? extends Class<? extends Exception>> exceptions = Arrays.asList(ConnectTimeoutException.class, ReadTimeoutException.class);
}
