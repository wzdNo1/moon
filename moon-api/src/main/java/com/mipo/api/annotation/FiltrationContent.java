package com.mipo.api.annotation;

import java.lang.annotation.*;

//文本，内容过滤
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FiltrationContent {
    // 1表示开启，0表示不开启
    int value() default  1;
}
