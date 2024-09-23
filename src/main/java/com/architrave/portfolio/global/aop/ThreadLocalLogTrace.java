package com.architrave.portfolio.global.aop;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace{

    private static final String DEEPER_SIGN = "-->";
    private static final String ESCAPE_SIGN = "<--";
    private static final String EX_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        Long startTime = System.currentTimeMillis();
        log.info("[{}]{}{}", traceId.getId(), createLog(DEEPER_SIGN, traceId.getDepth()) , message);

        return new TraceStatus(traceId, startTime, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e){
        Long endTime = System.currentTimeMillis();
        Long takesTime = endTime - status.getStartTime();
        TraceId traceId = status.getTraceId();
        if(e == null){
            log.info("[{}]{}{} it takes {}ms",
                    traceId.getId(),
                    createLog(ESCAPE_SIGN, traceId.getDepth()),
                    status.getMessage(),
                    takesTime);
        }else{
            log.info("[{}]{}{} it takes {}ms, ex: {}",
                    traceId.getId(),
                    createLog(EX_PREFIX, traceId.getDepth()),
                    status.getMessage(),
                    takesTime,
                    e.getMessage());
        }
        releaseTraceId();
    }

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();

        if(traceId == null){
            traceIdHolder.set(new TraceId());
        }else{
            traceIdHolder.set(traceId.createNextTraceId());
        }
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if(traceId.isFirstDepth()){
            traceIdHolder.remove();
        }else{
            traceIdHolder.set(traceId.createPrevTraceId());
        }
    }
    private static String createLog(String prefix, int depth){
        StringBuilder sb = new StringBuilder();
        if(prefix != DEEPER_SIGN) sb.append(prefix);
        for(int i=0; i<depth; i++){
            sb.append("|--|");
        }
        if(prefix == DEEPER_SIGN) sb.append(prefix);

        return sb.toString();
    }
}
