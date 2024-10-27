/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hoangtien2k3.reactify.filter.http;

import static reactor.core.scheduler.Schedulers.single;

import io.hoangtien2k3.reactify.LogUtils;
import io.hoangtien2k3.reactify.model.GatewayContext;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * ResponseLogFilter class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Log4j2
@AllArgsConstructor
@Component
public class ResponseLogFilter implements WebFilter, Ordered {
    private final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(cl -> cl.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
            .build();

    private static byte[] toByteArray(InputStream inStream) {
        final int bufferSize = 100;
        try (ByteArrayOutputStream swapStream = new ByteArrayOutputStream()) {
            byte[] buff = new byte[bufferSize];
            int bytesRead;

            while ((bytesRead = inStream.read(buff, 0, bufferSize)) != -1) {
                swapStream.write(buff, 0, bytesRead);
            }
            return swapStream.toByteArray();  // Trả về mảng byte đã đọc
        } catch (IOException e) {
            log.error("Error reading input stream", e);
            return new byte[0];
        }
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null && !gatewayContext.getReadResponseData()) {
            log.debug("[ResponseLogFilter] Properties Set Not To Read Response Data");
            return chain.filter(exchange);
        }
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @NotNull
            @Override
            public Mono<Void> writeWith(@NotNull Publisher<? extends DataBuffer> body) {
                final MediaType contentType = super.getHeaders().getContentType();
                if (LogUtils.legalLogMediaTypes.contains(contentType)) {
                    if (body instanceof Mono) {
                        final Mono<DataBuffer> monoBody = (Mono<DataBuffer>) body;
                        return super.writeWith(
                                monoBody.publishOn(single()).map(buffer -> logRequestBody(buffer, exchange)));
                    } else if (body instanceof Flux) {
                        final Flux<DataBuffer> monoBody = (Flux<DataBuffer>) body;
                        return super.writeWith(
                                monoBody.publishOn(single()).map(buffer -> logRequestBody(buffer, exchange)));
                    }
                }
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    private DataBuffer logRequestBody(DataBuffer buffer, ServerWebExchange exchange) {
        InputStream dataBuffer = buffer.asInputStream();
        byte[] bytes = toByteArray(dataBuffer);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null) {
            gatewayContext.setResponseBody(new String(bytes, StandardCharsets.UTF_8)); // Chuyển đổi bytes thành String với charset UTF-8
        } else {
            log.error("GatewayContext is null");
        }
        DataBufferUtils.release(buffer);
        return nettyDataBufferFactory.wrap(bytes);
    }

    /** {@inheritDoc} */
    @Override
    public int getOrder() {
        return 6;
    }

    public static class ResponseAdapter implements ClientHttpResponse {
        private final Flux<DataBuffer> flux;
        private final HttpHeaders headers;

        public ResponseAdapter(Publisher<? extends DataBuffer> body, HttpHeaders headers) {
            this.headers = headers;
            if (body instanceof Flux) {
                flux = (Flux<DataBuffer>) body;
            } else {
                flux = ((Mono<DataBuffer>) body).flux();
            }
        }

        @NotNull
        @Override
        public Flux<DataBuffer> getBody() {
            return flux;
        }

        @NotNull
        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        @Override
        public HttpStatus getStatusCode() {
            return null;
        }

        @Override
        public MultiValueMap<String, ResponseCookie> getCookies() {
            return null;
        }
    }
}
