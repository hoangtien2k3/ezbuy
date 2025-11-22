package com.ezbuy.triggerservice.order.job;

import com.ezbuy.triggerservice.client.OrderClient;
import com.ezbuy.triggerservice.dto.SyncOrderStateRequest;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SyncOrderStateJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(SyncOrderStateJob.class);

    private final OrderClient orderClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("=============SyncOrderStateJob execute");
        SyncOrderStateRequest request = new SyncOrderStateRequest();
        request.setStartTime(null);
        request.setEndTime(null);
        orderClient.syncOrderState(request).subscribe();
        logger.info("=============SyncOrderStateJob end");
    }
}
