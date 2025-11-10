/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
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
package com.ezbuy.core.model.logging;

import brave.Span;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Builder;
import lombok.Data;
import reactor.util.context.Context;

/**
 * Record representing log information within the system.
 *
 * @author hoangtien2k3
 */
@Data
@Builder
public class LoggerDTO {
    private final AtomicReference<Context> contextRef;
    private final Span newSpan;
    private final String service;
    private final Long startTime;
    private final Long endTime;
    private final String result;
    private final Object response;
    private final String logType;
    private final String actionType;
    private final Object[] args;
    private final String title;
}
