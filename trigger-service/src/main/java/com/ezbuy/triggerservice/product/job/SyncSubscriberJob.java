package com.ezbuy.triggerservice.product.job;

import com.ezbuy.triggerservice.client.ProductClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
@RequiredArgsConstructor
public class SyncSubscriberJob implements Job {

    private final ProductClient productClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("=============SyncSubscribeJob execute");
        productClient.syncSubscriber().subscribe();
        log.info("=============SyncSubscribeJob end");
    }
}
