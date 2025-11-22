package com.ezbuy.triggerservice.order.jobdetail;

import com.ezbuy.triggerservice.constant.JobGroup;
import com.ezbuy.triggerservice.constant.OrderJob;
import com.ezbuy.triggerservice.order.job.SyncOrderStateJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderJobConfiguration {

    @Bean(name = "syncOrderState")
    public JobDetail syncOrderState() {
        return JobBuilder.newJob()
                .ofType(SyncOrderStateJob.class)
                .withIdentity(OrderJob.SYNC_ORDER_STATE, JobGroup.ORDER_JOB)
                .storeDurably(true)
                .build();
    }
}
