package com.black.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ScheduleThread extends Thread {
    public ScheduleThread(){
        super("ScheduleThread-0");
    }

    @PostConstruct
    public void init(){
        LoggerFactory.getLogger(ScheduleThread.class).info("ScheduleThread start----");
        this.start();
    }

    @Override
    public void run() {
        //拉取任务，根据任务类型






    }
}
