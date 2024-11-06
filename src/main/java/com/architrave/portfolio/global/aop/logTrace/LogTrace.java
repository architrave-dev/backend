package com.architrave.portfolio.global.aop.logTrace;

public interface LogTrace {
    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}