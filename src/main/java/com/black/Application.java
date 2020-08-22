package com.black;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> LoggerFactory.getLogger("root").error("thread-"+thread.getName(),throwable));
    }

}
