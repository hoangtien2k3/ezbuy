package com.ezbuy.framework.config;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import brave.Tracing;

@Configuration
public class TracingConfiguration {

    @Bean
    public Tracer tracer(Tracing tracing) {
        return BraveTracer.NOOP;
    }

    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder().build();
    }
}
