package com.gpdi.origin.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public PlatformTransactionManager txManager(@Qualifier("dataSource")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
