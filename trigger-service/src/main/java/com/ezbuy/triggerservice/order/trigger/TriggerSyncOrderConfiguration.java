package com.ezbuy.triggerservice.order.trigger;

import com.ezbuy.triggerservice.config.JobFactory;
import com.ezbuy.triggerservice.constant.JobGroup;
import com.ezbuy.triggerservice.constant.OrderJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class TriggerSyncOrderConfiguration {

    private final JobFactory jobFactory;

    @Bean
    public Trigger triggerSyncOrderState(@Qualifier("syncOrderState") JobDetail jobDetail) {
        log.info("triggerSyncOrderState config");
        try {
            String jobName = OrderJob.SYNC_ORDER_STATE;
            return TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(jobName, JobGroup.ORDER_JOB)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                    .build();
        } catch (Exception e) {
            log.error("triggerSyncUserProfile error ", e);
            return null;
        }
    }
}
