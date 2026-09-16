package org.example.gift_api;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.example.gift_api.properties.AsyncProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(AsyncProperties.class)
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "${scheduler.present-processing.default-lock-at-most-for}")
@EnableAsync
public class GiftApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiftApiApplication.class, args);
    }



}
