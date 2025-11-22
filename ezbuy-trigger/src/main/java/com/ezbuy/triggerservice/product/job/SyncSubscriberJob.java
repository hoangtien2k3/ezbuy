package com.ezbuy.triggerservice.product.job;

import com.ezbuy.triggerservice.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class SyncSubscriberJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(SyncSubscriberJob.class);

    private final ProductClient productClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("=============SyncSubscribeJob execute");
        productClient.syncSubscriber().subscribe();
        logger.info("=============SyncSubscribeJob end");
    }
}
