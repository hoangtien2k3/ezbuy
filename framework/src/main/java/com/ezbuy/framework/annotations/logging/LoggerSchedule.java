package com.ezbuy.framework.annotations.logging;

import com.ezbuy.framework.factory.ObjectMapperFactory;
import com.ezbuy.framework.model.logging.LogField;
import com.ezbuy.framework.model.logging.LoggerDTO;
import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.framework.utils.RequestUtils;
import com.ezbuy.framework.utils.TruncateUtils;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ezbuy.framework.constants.Constants.MAX_BYTE;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class LoggerSchedule {
    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");

    @Scheduled(fixedDelay = 3000)
    public void scheduleSaveLogClick() {
        long analyId = System.currentTimeMillis();
        int numSuccess = 0;
        int numFalse = 0;
        List<LoggerDTO> records = LoggerQueue.getInstance().getRecords();
        for (LoggerDTO record : records) {
            try {
                process(record);
                numSuccess++;
            } catch (Exception e) {
                numFalse++;
                log.error("Error while handle record queue: {}", e.getMessage());
            }
        }
//        log.info("AsyncLog analyId {}: QueueSize: {}, addSuccess: {}, addFalse: {}, writeSuccess:{}, writeFalse:{}",
//                analyId, records.size(), LoggerQueue.getInstance().getCountSuccess(), LoggerQueue.getInstance().getCountFalse(), numSuccess, numFalse);
        LoggerQueue.getInstance().resetCount();
    }

    private void process(LoggerDTO record) {
        if (record != null) {
            String traceId = !DataUtil.isNullOrEmpty(record.getNewSpan().context().traceIdString()) ? record.getNewSpan().context().traceIdString() : "";
            String ipAddress = null;
            String requestId = null;
            if (record.getContextRef().get() != null) {
                if (record.getContextRef().get().hasKey(ServerWebExchange.class)) {
                    ServerWebExchange serverWebExchange = record.getContextRef().get().get(ServerWebExchange.class);
                    ServerHttpRequest request = serverWebExchange.getRequest();
                    ipAddress = RequestUtils.getIpAddress(request);

                    serverWebExchange.getRequest();
                    serverWebExchange.getRequest().getHeaders();
                    if (!DataUtil.isNullOrEmpty(serverWebExchange.getRequest().getHeaders().getFirst("Request-Id"))) {
                        requestId = serverWebExchange.getRequest().getHeaders().getFirst("Request-Id");
                    }
                }
            }

            String inputs = null;
            try {
                if (record.getArgs() != null) {
                    inputs = ObjectMapperFactory.getInstance().writeValueAsString(getAgrs(record.getArgs()));
                }
            } catch (Exception ex) {
            }

            String resStr = null;
            try {
                if (record.getResponse() instanceof Optional) {
                    Optional output = (Optional) record.getResponse();
                    if (output.isPresent()) {
                        resStr = ObjectMapperFactory.getInstance().writeValueAsString(output.get());
                    }
                } else {
                    if (record.getResponse() != null) {
                        resStr = ObjectMapperFactory.getInstance().writeValueAsString(record.getResponse());
                    }
                }
            } catch (Exception ex) {
            }
            try {
                inputs = TruncateUtils.truncate(inputs, MAX_BYTE);
                resStr = TruncateUtils.truncate(resStr, MAX_BYTE);
            } catch (Exception ex) {
                log.error("Truncate input/output error ", ex);
            }
            logInfo(new LogField(traceId, requestId, record.getService(), record.getEndTime() - record.getStartTime(), record.getLogType(),
                    record.getActionType(), record.getStartTime(), record.getEndTime(), ipAddress, record.getTitle(), inputs, resStr, record.getResult()));
        }
    }

    private void logInfo(LogField logField) {
        try {
            logPerf.info(ObjectMapperFactory.getInstance().writeValueAsString(logField));
        } catch (Exception ex) {
        }
    }

    private List<Object> getAgrs(Object[] args) {
        List<Object> listArg = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Mono) {
//                listArg.add(((Mono) args[i]).block());
//                skip
            } else if (args[i] instanceof ServerWebExchange) {
                //skip
            } else {
                listArg.add(args[i]);
            }
        }
        return listArg;
    }
}
