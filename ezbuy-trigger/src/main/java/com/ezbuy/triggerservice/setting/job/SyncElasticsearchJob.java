package com.ezbuy.triggerservice.setting.job;

import com.ezbuy.triggerservice.client.SettingClient;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class SyncElasticsearchJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(SyncElasticsearchJob.class);

    private final SettingClient settingClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("=============SyncElasticsearchJob execute");
        settingClient.syncNews().subscribe();
        settingClient.syncServices().subscribe();
        logger.info("=============SyncElasticsearchJob end");
    }
}
