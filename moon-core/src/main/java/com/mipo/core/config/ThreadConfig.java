package com.mipo.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadConfig {


    /**
     * 线程池的注解
     * @return
     */
    @Bean
    public ExecutorService executorService(){
        ExecutorService executorService =  new ThreadPoolExecutor(20,100,
                60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(500), Executors.defaultThreadFactory(),
               new ThreadPoolExecutor.CallerRunsPolicy());

        return executorService;
    }


}
