package com.ezbuy.triggerservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/jobs")
@Log4j2
@RequiredArgsConstructor
public class JobController {
    private final SchedulerFactoryBean schedulerFactoryBean;

    @GetMapping("/reload/{triggerName}/{triggerGroup}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public Mono<ResponseEntity> reloadErrorJob(
            @PathVariable("triggerName") String triggerName,
            @PathVariable("triggerGroup") String triggerGroup) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
        try {
            if (scheduler.getTriggerState(triggerKey).equals(Trigger.TriggerState.ERROR)) {
                scheduler.resetTriggerFromErrorState(triggerKey);
            }
        } catch (SchedulerException e) {
            log.error("Exception when reload Error Job", e);
            return Mono.just(ResponseEntity.badRequest().build());
        }
        return Mono.just(ResponseEntity.ok().build());
    }
}
