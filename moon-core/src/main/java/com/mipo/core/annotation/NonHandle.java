package com.mipo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对请求数据返回数据不做封装处理
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonHandle {
    //请求不处理
    boolean request() default true;
    //返回不处理
    boolean response() default true;
}
