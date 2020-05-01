package com.black.schedule;

import com.black.constant.Constants;
import com.black.po.TaskPo;
import com.black.repository.TaskRepository;
import com.black.service.EastMoneyPullService;
import com.black.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class ScheduleThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(ScheduleThread.class);

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EastMoneyPullService eastMoneyPullService;

    public ScheduleThread(){
        super("ScheduleThread-0");
    }

    @PostConstruct
    public void init(){
        log.info("ScheduleThread start----");
        this.start();
    }

    @Override
    public void run() {
        while (true){
            try {
                //股票数据每天四点一次
                pullPriceData();
                //财报数据每天四点一次
                pullFinanceData();
                //历史股票数据每分钟一次
                pullHistoryPriceData();
                //历史财报数据每分钟一次
                pullHistoryFinanceData();
                //信息补全每分钟一次
                checkNewStock();

                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                }
            } catch (Throwable e) {
                Helper.stack(e);
            }
        }
    }

    private void pullPriceData(){
        //有未完成任务吗，没有就拉
        Long endTime=System.currentTimeMillis();
        Long hour16=Date.from(LocalTime.of(16, 0).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()).getTime();
        PageRequest pageRequest=PageRequest.of(0,1,Sort.by("scheduleTime").descending());
        Page<TaskPo> recentTasks = taskRepository.findRecentTasks(endTime, Constants.PRICE_PULL, pageRequest);
        TaskPo recentTask = recentTasks.getContent().stream().findFirst().orElse(null);
        if(recentTask!=null&&recentTask.getStatus()==0){
            eastMoneyPullService.pullPriceData();
            recentTask.setStatus(1);
            recentTask.setScheduleCompleteTime(System.currentTimeMillis());
            taskRepository.save(recentTask);
            return;
        }
        //四点之后有拉过数据吗，没有就拉
        pageRequest=PageRequest.of(0,1,Sort.by("scheduleCompleteTime").descending());
        Page<TaskPo> completeTasks = taskRepository.findCompleteTasks(endTime, Constants.PRICE_PULL, pageRequest);
        TaskPo completeTask = completeTasks.getContent().stream().findFirst().orElse(null);
        if(completeTask!=null&&completeTask.getScheduleCompleteTime()>hour16){
            return;
        }
        //现在过了四点吗
        if(endTime>hour16){
            eastMoneyPullService.pullPriceData();
            return;
        }
    }

    private void pullFinanceData(){
        //有未完成任务吗，没有就拉
        Long endTime=System.currentTimeMillis();
        Long hour16=Date.from(LocalTime.of(16, 0).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()).getTime();
        PageRequest pageRequest=PageRequest.of(0,1,Sort.by("scheduleTime").descending());
        Page<TaskPo> recentTasks = taskRepository.findRecentTasks(endTime, Constants.FINANCE_PULL, pageRequest);
        TaskPo recentTask = recentTasks.getContent().stream().findFirst().orElse(null);
        if(recentTask.getStatus()==0){
            eastMoneyPullService.pullFinanceData();
            return;
        }
        //四点之后有拉过数据吗，没有就拉
        pageRequest=PageRequest.of(0,1,Sort.by("scheduleCompleteTime").descending());
        Page<TaskPo> completeTasks = taskRepository.findCompleteTasks(endTime, Constants.FINANCE_PULL, pageRequest);
        TaskPo completeTask = completeTasks.getContent().stream().findFirst().orElse(null);
        if(completeTask!=null&&completeTask.getScheduleCompleteTime()>hour16){
            return;
        }
        //现在过了四点吗
        if(endTime>hour16){
            eastMoneyPullService.pullFinanceData();
            return;
        }
    }

    private void pullHistoryPriceData(){
        //是否拉取完成
    }

    private void pullHistoryFinanceData(){
        //是否拉取完成

    }

    private void checkNewStock(){

    }

    private void checkInfoComplete(){

    }
}
