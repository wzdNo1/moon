package com.mipo.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: lyy
 * @Description:
 * @Date: 2019-08-01 11:42
 */
//@MapperScan 扫瞄到mapper
//@EnableTransactionManagement  开启事务
//@scanBasePackages 扫面需要注册到容器的组件
//@ServletComponentScan("com.mipo.api.xss")
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class}, scanBasePackages = {"com.mipo"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
