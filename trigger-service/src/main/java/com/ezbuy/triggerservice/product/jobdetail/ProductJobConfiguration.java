package com.ezbuy.triggerservice.product.jobdetail;

import com.ezbuy.triggerservice.constant.JobGroup;
import com.ezbuy.triggerservice.constant.ProductJob;
import com.ezbuy.triggerservice.product.job.*;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductJobConfiguration {

    @Bean(name = "syncProductFilter")
    public JobDetail syncProductFilter() {
        return JobBuilder.newJob().ofType(SyncProductFilterJob.class)
                .withIdentity(ProductJob.SYNC_PRODUCT_FILTER, JobGroup.PRODUCT_JOB)
                .storeDurably(true)
                .build();
    }

    @Bean(name = "syncSubscriberJob")
    public JobDetail syncAuthRetry() {
        return JobBuilder.newJob().ofType(SyncSubscriberJob.class)
                .withIdentity(ProductJob.SYNC_SUBSCRIBER, JobGroup.PRODUCT_JOB)
                .storeDurably(true)
                .build();
    }

    @Bean(name = "syncDailyReportJob")
    public JobDetail syncDailyReport() {
        return JobBuilder.newJob().ofType(SyncDailyReportJob.class)
                .withIdentity(ProductJob.SYNC_DAILY_REPORT, JobGroup.PRODUCT_JOB)
                .storeDurably(true)
                .build();
    }

    @Bean(name = "unlockVoucherJob")
    public JobDetail UnlockVoucher() {
        return JobBuilder.newJob().ofType(UnlockVoucherJob.class)
                .withIdentity(ProductJob.UNLOCK_VOUCHER, JobGroup.PRODUCT_JOB)
                .storeDurably(true)
                .build();
    }

    @Bean(name = "unlockVoucherTransactionJob")
    public JobDetail UnlockVoucherTransaction() {
        return JobBuilder.newJob().ofType(UnlockVoucherTransactionJob.class)
                .withIdentity(ProductJob.UNLOCK_VOUCHER_TRANSACTION, JobGroup.PRODUCT_JOB)
                .storeDurably(true)
                .build();
    }

    @Bean(name = "insertVoucherJob")
    public JobDetail insertVoucherJob() {
        return JobBuilder.newJob().ofType(InsertVoucherJob.class)
            .withIdentity(ProductJob.INSERT_VOUCHER, JobGroup.PRODUCT_JOB)
            .storeDurably(true)
            .build();
    }
}
