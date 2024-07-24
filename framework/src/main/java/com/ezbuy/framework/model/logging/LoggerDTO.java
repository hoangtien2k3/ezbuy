package com.ezbuy.framework.model.logging;

import java.util.concurrent.atomic.AtomicReference;

import brave.Span;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.util.context.Context;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggerDTO {
    AtomicReference<Context> contextRef;
    Span newSpan;
    String service;
    Long startTime;
    Long endTime;
    String result;
    Object response;
    String logType;
    String actionType;
    Object[] args;
    String title;
}
