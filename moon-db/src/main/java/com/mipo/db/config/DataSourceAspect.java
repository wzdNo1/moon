package com.mipo.db.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
  * @Description: 优先判断注解；其次判断方法名前缀
**/
@Slf4j
@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("execution(* com.mipo.db.dao..*.*(..))")
    public void aspect(){}

    @Before("aspect()")
    public void before(JoinPoint point){
        String className = point.getTarget().getClass().getName();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = methodSignature.getName();
        String request = className + "." + methodName + "(" + StringUtils.join(point.getArgs(), ",") + ")";
        log.debug("[DataSourceAspect]" + request);

        DsType annotation = method.getAnnotation(DsType.class);
        if(null != annotation){
            if(annotation.type().equals(DsTypeEnum.READ)){
                HandleDatasource.addDatasource("read");
            }else{
                HandleDatasource.addDatasource("write");
            }
            log.info("[DataSourceAspect]" + request + ",注解设置数据源为:" + annotation.type());
            return;
        }

        try {
            CDataSource.METHODTYPE.forEach((key, value) -> {
                CDataSource.METHODTYPE.get(key)
                        .stream()
                        .filter(type -> methodName.startsWith(type))
                        .forEach(type -> {
                           HandleDatasource.addDatasource(key);
                           log.debug("[DataSourceAspect]" + request + ",方法名前缀使用数据源为write");
                        });
            });
        }catch (Exception e){
            log.error("[DataSourceAspect]" + request + ",处理异常默认使用数据源为write", e);
            HandleDatasource.addDatasource("write");
        }
    }

    @After("aspect()")
    public void after(JoinPoint point){
        HandleDatasource.clear();
    }

}
