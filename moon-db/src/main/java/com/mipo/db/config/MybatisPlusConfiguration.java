package com.mipo.db.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageInterceptor;
import com.mipo.db.plugin.QueryInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: lyy
 * @Description:
 * @Date: 2019-08-01 13:50
 */
@Configuration
@MapperScan("com.mipo.db.dao")
public class MybatisPlusConfiguration {

    /*
     * 分页插件，自动识别数据库类型
     * 多租户，请参考官网【插件扩展】
     */
    /*@Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 PageHelper 的支持
        paginationInterceptor.setLocalPage(true);
        return paginationInterceptor;
    }*/

/*    *//**
     * SQL执行效率插件
     *//*
    @Bean
    @Profile({"dev"})// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setMaxTime(1000);
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }*/

    @Bean(name = "db1")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db1" )
    public DataSource db1() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "db2")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db2" )
    public DataSource db2() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 动态数据源配置
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource(@Qualifier("db1") DataSource db1, @Qualifier("db2") DataSource db2) {
        CDataSource multipleDataSource = new CDataSource();
        Map< Object, Object > targetDataSources = new HashMap<>();
        targetDataSources.put(DsTypeEnum.WRITE, db1);
        targetDataSources.put(DsTypeEnum.READ, db2);
        //添加数据源
        multipleDataSource.setTargetDataSources(targetDataSources);
        //设置默认数据源
        multipleDataSource.setDefaultTargetDataSource(db1);
        return multipleDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(multipleDataSource(db1(),db2()));
    }
   /* @Bean
    public PageHelper getPageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }*/
//    @Bean("sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource(multipleDataSource(db1(),db2()));
//        //sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*/*Mapper.xml"));
//
//        MybatisConfiguration configuration = new MybatisConfiguration();
//        //configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
//        configuration.setJdbcTypeForNull(JdbcType.NULL);
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.setCacheEnabled(false);
//        sqlSessionFactory.setConfiguration(configuration);
//        sqlSessionFactory.setPlugins(new Interceptor[]{ //PerformanceInterceptor(),OptimisticLockerInterceptor()
//                paginationInterceptor() //添加分页功能
//        });
//        //sqlSessionFactory.setGlobalConfig(globalConfiguration());
//        return sqlSessionFactory.getObject();
//    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(multipleDataSource(db1(),db2()));
        String path = "classpath*:com.mipo.db.dao/*.xml";
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(path));
        //添加分页插件
        addPageHelperPlugin(factoryBean);
        return factoryBean.getObject();
    }

    private void addPageHelperPlugin(SqlSessionFactoryBean sessionFactoryBean) throws Exception {
        PageInterceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        interceptor.setProperties(properties);

        org.apache.ibatis.session.Configuration configuration = sessionFactoryBean.getObject().getConfiguration();
        configuration.addInterceptor(interceptor);
        configuration.addInterceptor(new QueryInterceptor());
    }

}
