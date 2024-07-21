package com.ezbuy.framework.annotations.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@RequiredArgsConstructor
@Log4j2
public class LoggerAspect {
    private final LoggerAspectUtils loggerAspectUtils;

    @Pointcut("(execution(* com.ezbuy.framework.*.controller..*(..)) || " +
            "execution(* com.ezbuy.framework.*.service..*(..))  ||  " +
            "execution(* com.ezbuy.framework.*.repository..*(..)) || " +
            "execution(* com.ezbuy.framework.*.client..*(..))) &&" +
            "!execution(* org.springframework.boot.actuate..*(..))"
    )
    public void performancePointCut() {
    }

    @Pointcut("@annotation(com.ezbuy.framework.annotations.LogPerformance)")
    private void logPerfMethods() {
    }

    @Around("performancePointCut() || logPerfMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return loggerAspectUtils.logAround(joinPoint);
    }
}
