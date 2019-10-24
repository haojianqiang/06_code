package com.gpdi.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import pub.spring.bean.ClassNameGenerator;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan(nameGenerator = ClassNameGenerator.class)
public class SpringCloudScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudScheduleApplication.class, args);
    }

    /**
     * 定时任务多线程处理
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("wisdom-farm-task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }
}
