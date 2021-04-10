package com.anshuman.spring.reactive.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.anshuman.spring.reactive.resource.VehicleReactiveResource.*(..)) ||" +
            "execution(* com.anshuman.spring.reactive.service.VehicleReactiveService.*(..)) ||" +
            "execution(* com.anshuman.spring.reactive.repository.VehicleReactiveRepository.*(..)) ")
    public Object logAllMethods(ProceedingJoinPoint joinPoint) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();

        log.debug("Entered {}.{} thread={}", className, methodName, Thread.currentThread().getName());

        //Measure method execution time
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        //Log method execution time
        log.debug("Exited {}.{} with executionTime={}ms", className, methodName, stopWatch.getTotalTimeMillis());

        return result;
    }
}
