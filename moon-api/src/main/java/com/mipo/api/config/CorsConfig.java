package com.mipo.api.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.mipo.api.interceptor.AuthorizationInterceptor;
import com.mipo.api.interceptor.FiltrationContentceptor;
import com.mipo.api.interceptor.RepeatInterceptor;
import com.mipo.api.resolver.CMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

/**
 * @Author: lyy
 * @Description:
 * @Date: 2019-08-05 14:11
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;
    @Autowired
    private RepeatInterceptor repeatInterceptor;
    @Autowired
    private FiltrationContentceptor filtrationContentceptor;

    /**
     * 验签注册,对登录，swagger中的请求不进行拦截，注册不进行验签处理
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册验签
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger**","/swagger-resources/**","/webjars/**","/v2/**","/configuration/**");
//        //注册重复提交
//        registry.addInterceptor(repeatInterceptor).addPathPatterns("/**");
//        //数据鉴别
//        registry.addInterceptor(filtrationContentceptor).addPathPatterns("/**");
    }


    /**
     * 跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }

    /**
     * 自定义的消息转换器
     * @param converters
     */
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new MappingJackson2HttpMessageConverter());
        //converters.add(0,new FastJsonHttpMessageConverter());

        //converters.add(new CMessageConverter());
    }

    /**
     *
     * @param registry
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        super.addResourceHandlers(registry);
    }

    /*@Bean
    public HttpMessageConverters fastJsonConfigure(){
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //日期格式化
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

        // 中文乱码解决方案
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);//设定json格式且编码为UTF-8
        converter.setSupportedMediaTypes(mediaTypes);

        converter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(converter);
    }*/
}
