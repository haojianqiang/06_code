package com.gpdi.miniprogram.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

/**
 * 事务管理器配置
 */
@Configuration
@EnableTransactionManagement
public class TransactionManagementConfig {
    @Bean("txManager")
    @Primary
    public PlatformTransactionManager txManager(@Qualifier("dataSource")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("txManager2")
    public PlatformTransactionManager txManager2(@Qualifier("dataSource2")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
