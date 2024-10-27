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
package io.hoangtien2k3.reactify.filter.webclient;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * WebClientMonitoringFilter class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@Data
@RequiredArgsConstructor
public class WebClientMonitoringFilter implements ExchangeFilterFunction {
    private static final String METRICS_WEBCLIENT_START_TIME =
            WebClientMonitoringFilter.class.getName() + ".START_TIME";
    private final MeterRegistry meterRegistry;

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
        long startTime = System.nanoTime();
        return exchangeFunction
                .exchange(clientRequest)
                .doOnEach(signal -> {
                    if (!signal.isOnComplete()) {
                        ClientResponse clientResponse = signal.get();
                        Throwable throwable = signal.getThrowable();
                        long duration = System.nanoTime() - startTime;
                        logMonitoringMetrics(clientRequest, clientResponse, throwable, duration);
                    }
                })
                .contextWrite((contextView) -> contextView.put(METRICS_WEBCLIENT_START_TIME, System.nanoTime()));
    }

    private void logMonitoringMetrics(ClientRequest clientRequest, ClientResponse clientResponse, Throwable throwable, long duration) {
        Iterable<Tag> tags = createTags(clientRequest, clientResponse, throwable);
        Timer.builder("http.client.requests")
                .tags(tags)
                .description("Timer of WebClient operation")
                .publishPercentiles(0.95, 0.99)
                .register(meterRegistry)
                .record(duration, TimeUnit.NANOSECONDS);
        log.info("Monitoring WebClient API {}: {} s", tags, (double) duration / Math.pow(10, 9));
    }

    private Tags createTags(ClientRequest clientRequest, ClientResponse clientResponse, Throwable throwable) {
        // Tạo thẻ từ request, response và throwable
        Tags tags = Tags.of(
                "method", clientRequest.method().name(),
                "uri", clientRequest.url().toString()
        );
        if (clientResponse != null) {
            tags = tags.and("status", String.valueOf(clientResponse.statusCode().value()));
        }
        if (throwable != null) {
            tags = tags.and("error", throwable.getClass().getSimpleName());
        }
        return tags;
    }
}
