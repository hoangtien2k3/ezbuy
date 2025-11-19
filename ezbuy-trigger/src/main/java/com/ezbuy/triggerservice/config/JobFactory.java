package com.ezbuy.triggerservice.config;

import com.ezbuy.core.util.DataUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobFactory {
    private final JobConfigs jobConfigs;
    private final Map<String, String> jobMap = new HashMap<>();

    public JobFactory(JobConfigs jobConfigs) {
        this.jobConfigs = jobConfigs;
        initJobMap();
    }

    public String getCronJobByName(String name) {
        return jobMap.get(name);
    }

    private void initJobMap() {
        if (jobConfigs == null) {
            return;
        }
        List<JobProfile> jobProfiles = jobConfigs.getProfiles();
        if (jobProfiles == null) {
            return;
        }
        for (JobProfile jobProfile : jobProfiles) {
            String name = jobProfile.getName();
            if (!DataUtil.isNullOrEmpty(name)) {
                String existedCronJob = getCronJobByName(jobProfile.getName());
                if (existedCronJob != null) {
                    log.error("Job existed, will be override {}", name);
                } else {
                    jobMap.put(name, jobProfile.getCronJob());
                }
            }
        }
    }
}
