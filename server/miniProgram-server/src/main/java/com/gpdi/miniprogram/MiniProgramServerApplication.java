package com.gpdi.miniprogram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession
public class MiniProgramServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniProgramServerApplication.class, args);
	}

}
