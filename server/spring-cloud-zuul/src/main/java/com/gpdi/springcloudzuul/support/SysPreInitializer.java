package com.gpdi.springcloudzuul.support;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import pub.dao.mybatis.MyBatisDao;
import pub.dao.sql.dialect.DialectManager;
import pub.dao.sql.dialect.MysqlDialect;

import javax.annotation.PostConstruct;

/**
 * 初始化所有Bean之前需要先加载SysPreInitializer
 * 根据数据库类型加载不同的Dialect
 */
@Configuration
public class SysPreInitializer implements BeanPostProcessor{

    @PostConstruct
    public void afterPropertiesSet(){
        System.out.println(" ---- SysPreInitializer ---- ");
        DialectManager.currentDialect = new MysqlDialect();
        MyBatisDao.generators.put("uuid",new UuidGenerator());
    }
}
