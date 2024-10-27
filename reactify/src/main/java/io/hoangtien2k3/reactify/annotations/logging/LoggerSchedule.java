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
package io.hoangtien2k3.reactify.annotations.logging;

import static io.hoangtien2k3.reactify.constants.Constants.MAX_BYTE;

import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.RequestUtils;
import io.hoangtien2k3.reactify.TruncateUtils;
import io.hoangtien2k3.reactify.factory.ObjectMapperFactory;
import io.hoangtien2k3.reactify.model.logging.LogField;
import io.hoangtien2k3.reactify.model.logging.LoggerDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * LoggerSchedule class.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class LoggerSchedule {
    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");

    /**
     * <p>
     * scheduleSaveLogClick.
     * </p>
     */
    @Scheduled(fixedDelay = 3000)
    public void scheduleSaveLogClick() {
        long analyzeId = System.currentTimeMillis();
        int successCount = 0;
        int failureCount = 0;
        LoggerQueue loggerQueue = LoggerQueue.getInstance();
        List<LoggerDTO> records = loggerQueue.getRecords();
        for (LoggerDTO record : records) {
            try {
                process(record);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                log.error("Error while processing record in queue: {}", e.getMessage());
            }
        }
//        log.info("AsyncLog analyzeId {}: QueueSize: {}, addSuccess: {}, addFalse: {}, writeSuccess: {}, writeFalse: {}",
//                analyzeId, records.size(), loggerQueue.getCountSuccess(), loggerQueue.getCountFalse(), successCount, failureCount);
        loggerQueue.resetCount();
    }

    private void process(LoggerDTO record) {
        if (record == null) return;
        String traceIdStr = record.getNewSpan().context().traceIdString();
        String traceId = !DataUtil.isNullOrEmpty(traceIdStr) ? traceIdStr : "";
        String ipAddress = getIpAddressFromRecord(record);
        String requestId = getRequestIdFromRecord(record);
        String inputs = serializeArgs(record.getArgs());
        String resStr = serializeResponse(record.getResponse());
        inputs = truncateSafely(inputs);
        resStr = truncateSafely(resStr);
        logInfo(new LogField(
                traceId,
                requestId,
                record.getService(),
                record.getEndTime() - record.getStartTime(),
                record.getLogType(),
                record.getActionType(),
                record.getStartTime(),
                record.getEndTime(),
                ipAddress,
                record.getTitle(),
                inputs,
                resStr,
                record.getResult()));
    }

    private String getIpAddressFromRecord(LoggerDTO record) {
        return Optional.ofNullable(record.getContextRef().get())
                .filter(context -> context.hasKey(ServerWebExchange.class))
                .map(context -> {
                    ServerWebExchange exchange = context.get(ServerWebExchange.class);
                    return RequestUtils.getIpAddress(exchange.getRequest());
                }).orElse(null);
    }

    private String getRequestIdFromRecord(LoggerDTO record) {
        return Optional.ofNullable(record.getContextRef().get())
                .filter(context -> context.hasKey(ServerWebExchange.class))
                .map(context -> {
                    ServerWebExchange exchange = context.get(ServerWebExchange.class);
                    return exchange.getRequest().getHeaders().getFirst("Request-Id");
                }).orElse(null);
    }

    private String serializeArgs(Object[] args) {
        try {
            if (args != null) {
                return ObjectMapperFactory.getInstance().writeValueAsString(getValidArgs(args));
            }
        } catch (Exception ex) {
            log.error("Error serializing arguments: {}", ex.getMessage());
        }
        return null;
    }

    private String serializeResponse(Object response) {
        try {
            if (response instanceof Optional<?> output) {
                if (output.isPresent()) {
                    return ObjectMapperFactory.getInstance().writeValueAsString(output.get());
                }
            } else {
                if (response != null) {
                    return ObjectMapperFactory.getInstance().writeValueAsString(response);
                }
            }
        } catch (Exception ex) {
            log.error("Error serializing response: {}", ex.getMessage());
        }
        return null;
    }

    private List<Object> getValidArgs(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> !(arg instanceof Mono) && !(arg instanceof ServerWebExchange))
                .collect(Collectors.toList());
    }

    private String truncateSafely(String str) {
        try {
            return TruncateUtils.truncate(str, MAX_BYTE);
        } catch (Exception ex) {
            log.error("Truncate input/output error ", ex);
            return str;
        }
    }

    private void logInfo(LogField logField) {
        try {
            logPerf.info(ObjectMapperFactory.getInstance().writeValueAsString(logField));
        } catch (Exception ex) {
            log.error("Error while logging info: {}", ex.getMessage());
        }
    }
}
