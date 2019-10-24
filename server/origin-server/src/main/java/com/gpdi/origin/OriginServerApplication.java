package com.gpdi.origin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.client.RestTemplate;
import pub.spring.bean.ClassNameGenerator;

import java.nio.charset.Charset;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession
@ComponentScan(nameGenerator = ClassNameGenerator.class)
public class OriginServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OriginServerApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		// 解决中文乱码
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}
}
