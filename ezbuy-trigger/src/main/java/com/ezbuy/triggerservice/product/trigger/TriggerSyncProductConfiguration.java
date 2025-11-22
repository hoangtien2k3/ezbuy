package com.ezbuy.triggerservice.product.trigger;

import com.ezbuy.triggerservice.config.JobFactory;
import com.ezbuy.triggerservice.constant.JobGroup;
import com.ezbuy.triggerservice.constant.ProductJob;
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
public class TriggerSyncProductConfiguration {

    private final Logger logger = LoggerFactory.getLogger(TriggerSyncProductConfiguration.class);

    private final JobFactory jobFactory;

    @Bean
    public Trigger triggerSyncProductFilter(@Qualifier("syncProductFilter") JobDetail jobDetail) {
        logger.info("triggerSyncProductFilter config");
        try {
            String jobName = ProductJob.SYNC_PRODUCT_FILTER;
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobName, JobGroup.PRODUCT_JOB)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                    .build();
        } catch (Exception e) {
            logger.error("triggerSyncUserProfile error ", e);
            return null;
        }
    }

    @Bean
    public Trigger triggerSyncSubscribeJob(@Qualifier("syncSubscriberJob") JobDetail jobDetail) {
        logger.info("triggerSyncSubscriberJob config");
        try {
            String jobName = ProductJob.SYNC_SUBSCRIBER;
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobName, JobGroup.PRODUCT_JOB)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                    .build();
        } catch (Exception e) {
            logger.error("triggerSubscriber error ", e);
            return null;
        }
    }
}
