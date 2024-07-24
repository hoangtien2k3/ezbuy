package com.ezbuy.framework.filter.webclient;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public record WebClientMonitoringFilter(MeterRegistry meterRegistry) implements ExchangeFilterFunction {
    private static final String METRICS_WEBCLIENT_START_TIME =
            WebClientMonitoringFilter.class.getName() + ".START_TIME";
    //    private WebClientExchangeTagsProvider tagsProvider = new DefaultWebClientExchangeTagsProvider();

    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
        return exchangeFunction
                .exchange(clientRequest)
                .doOnEach(signal -> {
                    if (!signal.isOnComplete()) {
                        Long startTime = signal.getContextView().get(METRICS_WEBCLIENT_START_TIME);
                        ClientResponse clientResponse = signal.get();
                        Throwable throwable = signal.getThrowable();
                        //                Iterable<Tag> tags = tagsProvider.tags(clientRequest, clientResponse,
                        // throwable);
                        //                Timer.builder("http.client.requests ")
                        //                        .tags(tags)
                        //                        .description("Timer of WebClient operation")
                        //                        .publishPercentiles(0.95, 0.99)
                        //                        .register(meterRegistry)
                        //                        .record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                        //                log.info("Monitoring webClient API {}: {} s", tags, (double)
                        // (System.nanoTime() - startTime) / Math.pow(10, 9));
                    }
                })
                .contextWrite((contextView) -> contextView.put(METRICS_WEBCLIENT_START_TIME, System.nanoTime()));
    }
}
