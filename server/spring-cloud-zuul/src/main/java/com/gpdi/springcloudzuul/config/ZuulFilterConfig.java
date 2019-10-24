package com.gpdi.springcloudzuul.config;

import com.gpdi.springcloudzuul.filter.RedisSessionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Zuul配置文件
 */
@Configuration
public class ZuulFilterConfig {
    @Bean
    public RedisSessionFilter redisSessionFilter(){
        return new RedisSessionFilter();
    }
}
