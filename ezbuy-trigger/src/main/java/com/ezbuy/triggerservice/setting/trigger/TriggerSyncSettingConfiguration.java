package com.ezbuy.triggerservice.setting.trigger;

import com.ezbuy.triggerservice.config.JobFactory;
import com.ezbuy.triggerservice.constant.JobGroup;
import com.ezbuy.triggerservice.constant.SettingJob;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TriggerSyncSettingConfiguration {

    private final Logger logger = LoggerFactory.getLogger(TriggerSyncSettingConfiguration.class);

    private final JobFactory jobFactory;

    @Bean
    public Trigger triggerSyncElasticsearch(@Qualifier("syncElasticsearch") JobDetail jobDetail) {
        logger.info("triggerSyncElasticsearch config");
        try {
            String jobName = SettingJob.SYNC_ELASTICSEARCH;
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobName, JobGroup.SETTING_JOB)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                    .build();
        } catch (Exception e) {
            logger.error("triggerSyncElasticsearch error ", e);
            return null;
        }
    }
}
