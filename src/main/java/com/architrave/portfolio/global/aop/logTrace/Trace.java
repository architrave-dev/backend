package com.architrave.portfolio.global.aop.logTrace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})     //아예 적용 안되는 중 고치셈
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
}
