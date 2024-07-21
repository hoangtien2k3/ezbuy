package com.ezbuy.framework.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Objects;

public class RequestUtils {
    /**
     * get Real Ip Address
     *
     * @param request ServerHttpRequest
     * @return
     * @author hoangtien2k3
     */
    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-original-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("x-forwarded-for");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        }
        if (ip != null && ip.length() > 15 && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    /**
     * get offline cache key
     *
     * @return
     */
    public static String getOfflineCacheKey(String ip, int port) {
        if (ip != null) {
            return ip + ":" + port;
        }
        return null;
    }
}
