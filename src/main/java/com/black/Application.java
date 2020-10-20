package com.black;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("Application begin ----- ");
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> LoggerFactory.getLogger("root").error("thread-"+thread.getName(),throwable));
        SpringApplication.run(Application.class);
    }

}
