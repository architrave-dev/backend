package com.architrave.portfolio.global.aop.logTrace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TraceStatus {
    private TraceId traceId;
    private Long startTime;
    private String message;
}
