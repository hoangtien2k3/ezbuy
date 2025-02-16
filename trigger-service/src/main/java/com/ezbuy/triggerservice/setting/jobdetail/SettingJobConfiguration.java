package com.ezbuy.triggerservice.setting.jobdetail;

import com.ezbuy.triggerservice.constant.JobGroup;
import com.ezbuy.triggerservice.constant.SettingJob;
import com.ezbuy.triggerservice.setting.job.SyncElasticsearchJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettingJobConfiguration {

    @Bean(name = "syncElasticsearch")
    public JobDetail syncElasticsearch() {
        return JobBuilder.newJob().ofType(SyncElasticsearchJob.class)
                .withIdentity(SettingJob.SYNC_ELASTICSEARCH, JobGroup.SETTING_JOB)
                .storeDurably(true)
                .build();
    }
}
