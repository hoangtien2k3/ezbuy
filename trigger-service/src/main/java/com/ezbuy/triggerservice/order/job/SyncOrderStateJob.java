package com.ezbuy.triggerservice.order.job;

import com.ezbuy.ordermodel.dto.request.SyncOrderStateRequest;
import com.ezbuy.triggerservice.client.OrderClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SyncOrderStateJob implements Job {

    private final OrderClient orderClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("=============SyncOrderStateJob execute");
        SyncOrderStateRequest request = new SyncOrderStateRequest();
        request.setStartTime(null);
        request.setEndTime(null);
        orderClient.syncOrderState(request).subscribe();
        log.info("=============SyncOrderStateJob end");
    }
}
