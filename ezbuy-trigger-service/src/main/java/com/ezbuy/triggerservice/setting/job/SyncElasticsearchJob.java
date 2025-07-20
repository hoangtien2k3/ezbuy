package com.ezbuy.triggerservice.setting.job;

import com.ezbuy.triggerservice.client.SettingClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
@RequiredArgsConstructor
public class SyncElasticsearchJob implements Job {

    private final SettingClient settingClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("=============SyncElasticsearchJob execute");
        settingClient.syncNews().subscribe();
        settingClient.syncServices().subscribe();
        log.info("=============SyncElasticsearchJob end");
    }
}
