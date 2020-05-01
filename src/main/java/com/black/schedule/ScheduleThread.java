package com.black.schedule;

import com.black.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ScheduleThread extends Thread {
    @Autowired
    private TaskRepository taskRepository;

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

        //当天股票数据
        //当天财报数据

        //前两天的

        //检测是否出现新的股票代码，更新股票信息

        //检测是否有数据不全的股票，补全数据







    }
}
