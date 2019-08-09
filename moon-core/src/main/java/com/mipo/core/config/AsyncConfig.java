package com.mipo.core.config;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    @Autowired
    private ExecutorService executorService;

    @Override
    public Executor getAsyncExecutor() {
        return executorService;
    }

    /**
     * 异步执行时的异常处理
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        /*return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                logger.error("异步执行错误,method:{}, params:{},错误信息:{}",method.toString() , JSONObject.toJSONString(params));
                logger.error("异步执行错误,", ex);
            }
        };*/
        return (Throwable ex, Method method, Object... params) -> {
            logger.error("异步执行错误,method:{}, params:{},错误信息:{}",method.toString() , JSONObject.toJSONString(params));
            logger.error("异步执行错误,", ex);
        };
    }

}
