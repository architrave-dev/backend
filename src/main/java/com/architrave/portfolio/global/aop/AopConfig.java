package com.architrave.portfolio.global.aop;

import com.architrave.portfolio.global.aop.logTrace.LogTrace;
import com.architrave.portfolio.global.aop.logTrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public LogTrace logTrace(){
        return new ThreadLocalLogTrace();
    }
}
