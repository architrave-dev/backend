package com.architrave.portfolio.global.aop.logTrace;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TraceId {
    private String id;
    private int depth;


    public TraceId(){
        this.id = createId();
        this.depth = 0;
    }

    private String createId() {
        return UUID.randomUUID().toString().substring(0,8);
    }

    public TraceId createNextTraceId(){
        return new TraceId(id, depth +1);
    }

    public TraceId createPrevTraceId(){
        return new TraceId(id, depth -1);
    }

    public boolean isFirstDepth(){
        return depth == 0;
    }
}
