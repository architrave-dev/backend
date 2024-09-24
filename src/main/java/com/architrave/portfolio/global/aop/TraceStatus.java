package com.architrave.portfolio.global.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TraceStatus {
    private TraceId traceId;
    private Long startTime;
    private String message;
}
