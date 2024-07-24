package com.ezbuy.framework.model;

import reactor.core.publisher.Mono;

public interface SagaStep {

    boolean complete();

    Mono<StepResult> execute();

    Mono<Boolean> revert();
}
