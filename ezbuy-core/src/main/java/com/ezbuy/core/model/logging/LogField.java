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

import lombok.Builder;
import lombok.Data;

/**
 * Record representing log fields for tracking service requests and responses.
 *
 * @author hoangtien2k3
 */
@Data
@Builder
public class LogField {
    private final String traceId;
    private final String requestId;
    private final String service;
    private final Long duration;
    private final String logType;
    private final String actionType;
    private final Long startTime;
    private final Long endTime;
    private final String clientAddress;
    private final String title;
    private final String inputs;
    private final String response;
    private final String result;
}
