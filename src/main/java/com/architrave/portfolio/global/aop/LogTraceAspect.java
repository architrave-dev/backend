package com.architrave.portfolio.global.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class LogTraceAspect {

    private final LogTrace logTrace;

    @Pointcut("@annotation(com.architrave.portfolio.global.aop.Trace)")
    public void methodLevel(){};

    @Pointcut("within(@com.architrave.portfolio.global.aop.Trace *)")
    public void classLevel(){};

    @Pointcut("methodLevel() || classLevel()")
    public void logTracePointcut() {}

    @Around("logTracePointcut()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        TraceStatus status = null;

        try {
            String message = joinPoint.getSignature().toShortString(); //Class이름.method 이름
            status = logTrace.begin(message);

            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }
}
