package com.mipo.core.express.config;


import com.mipo.core.express.ExpressService;
import org.springframework.context.annotation.Bean;

//@Configuration
//@EnableConfigurationProperties(ExpressProperties.class)  获取配置文件属性
public class ExpressAutoConfiguration {

    private final ExpressProperties properties;

    public ExpressAutoConfiguration(ExpressProperties properties) {
        this.properties = properties;
    }

    //@Bean
    public ExpressService expressService() {
        ExpressService expressService = new ExpressService();
        expressService.setProperties(properties);
        return expressService;
    }

}
