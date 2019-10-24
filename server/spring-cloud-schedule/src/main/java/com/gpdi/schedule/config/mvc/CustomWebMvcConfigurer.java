package com.gpdi.schedule.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pub.spring.web.mvc.controller.CustomHandlerMapping;

/**
 * 自定义Spring MVC 配置
 *
 * @author zzj
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
 * @since 2019/8/7 14:27
 */
@Configuration
public class CustomWebMvcConfigurer extends WebMvcConfigurationSupport {

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new CustomHandlerMapping();
    }
}
