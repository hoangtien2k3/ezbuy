package com.ezbuy.triggerservice.product.trigger;

import com.ezbuy.triggerservice.config.JobFactory;
import com.ezbuy.triggerservice.constant.JobGroup;
import com.ezbuy.triggerservice.constant.ProductJob;
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
public class TriggerSyncProductConfiguration {
    private final JobFactory jobFactory;

    @Bean
    public Trigger triggerSyncProductFilter(@Qualifier("syncProductFilter") JobDetail jobDetail) {
        log.info("triggerSyncProductFilter config");
        try {
            String jobName = ProductJob.SYNC_PRODUCT_FILTER;
            return TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(jobName, JobGroup.PRODUCT_JOB)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                    .build();
        } catch (Exception e) {
            log.error("triggerSyncUserProfile error ", e);
            return null;
        }
    }

    @Bean
    public Trigger triggerSyncSubscribeJob(@Qualifier("syncSubscriberJob") JobDetail jobDetail) {
        log.info("triggerSyncSubscriberJob config");
        try {
            String jobName = ProductJob.SYNC_SUBSCRIBER;
            return TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(jobName, JobGroup.PRODUCT_JOB)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                    .build();
        } catch (Exception e) {
            log.error("triggerSubscriber error ", e);
            return null;
        }
    }

    @Bean
    public Trigger triggerSyncDailyReportJob(@Qualifier("syncDailyReportJob") JobDetail jobDetail) {
        log.info("triggerSyncDailyReportJob config");
        try {
            String jobName = ProductJob.SYNC_DAILY_REPORT;
            return TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(jobName, JobGroup.PRODUCT_JOB)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                    .build();
        } catch (Exception e) {
            log.error("triggerSyncDailyReportJob error ", e);
            return null;
        }
    }

    @Bean
    public Trigger triggerUnlockVoucherJob(@Qualifier("unlockVoucherJob") JobDetail jobDetail) {
        log.info("triggerUnlockVoucherJob config");
        try {
            String jobName = ProductJob.UNLOCK_VOUCHER;
            return TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(jobName, JobGroup.PRODUCT_JOB)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                    .build();
        } catch (Exception e) {
            log.error("triggerUnlockVoucherJob error ", e);
            return null;
        }
    }

    @Bean
    public Trigger triggerUnlockVoucherTransactionJob(@Qualifier("unlockVoucherTransactionJob") JobDetail jobDetail) {
        log.info("triggerUnlockVoucherTransactionJob config");
        try {
            String jobName = ProductJob.UNLOCK_VOUCHER_TRANSACTION;
            return TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(jobName, JobGroup.PRODUCT_JOB)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                    .build();
        } catch (Exception e) {
            log.error("triggerUnlockVoucherTransactionJob error ", e);
            return null;
        }
    }
    @Bean
    public Trigger triggerInsertVoucherJob(@Qualifier("insertVoucherJob") JobDetail jobDetail) {
        log.info("triggerInsertVoucherJob config");
        try {
            String jobName = ProductJob.INSERT_VOUCHER;
            return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobName, JobGroup.PRODUCT_JOB)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                .build();
        } catch (Exception e) {
            log.error("triggerInsertVoucherJob error ", e);
            return null;
        }
    }
}
