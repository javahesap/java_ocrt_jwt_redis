package com.sendika.bookstore.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Simple aspect that measures and logs the execution time of methods
 * annotated with {@link LogExecution}.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(logExecution)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecution logExecution) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.nanoTime() - start;
            String label = logExecution.value().isBlank() ? joinPoint.getSignature().toShortString() : logExecution.value();
            log.info("[AOP] {} executed in {} ms", label, TimeUnit.NANOSECONDS.toMillis(duration));
        }
    }
}
