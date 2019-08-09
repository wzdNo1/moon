package com.mipo.api.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ForbidRepeat {
    //默认禁止时间，单位为毫秒
    int value() default  2000;
}
