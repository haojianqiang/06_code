package com.gpdi.springcloudzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import pub.spring.bean.ClassNameGenerator;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableRedisHttpSession
@ComponentScan(nameGenerator = ClassNameGenerator.class)
public class SpringCloudZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudZuulApplication.class, args);
	}

}
