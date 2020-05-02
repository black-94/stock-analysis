package com.black.schedule;

import com.black.constant.Constants;
import com.black.po.ErrorPo;
import com.black.po.StockFinancePo;
import com.black.po.StockInfoPo;
import com.black.po.TaskPo;
import com.black.repository.ErrorRepository;
import com.black.repository.StockFinanceRepository;
import com.black.repository.StockInfoRepository;
import com.black.repository.TaskRepository;
import com.black.service.EastMoneyPullService;
import com.black.service.Finance163PullService;
import com.black.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ScheduleThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(ScheduleThread.class);

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EastMoneyPullService eastMoneyPullService;
    @Autowired
    private StockFinanceRepository stockFinanceRepository;
    @Autowired
    private StockInfoRepository stockInfoRepository;
    @Autowired
    ErrorRepository errorRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    Finance163PullService finance163PullService;

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
                //财报数据每天四点一次
                pullFinanceData();
                //信息补全每分钟一次
                checkNewStock();
                //股票数据每天四点一次
                pullPriceData();
                //历史股票数据每分钟一次
                pullHistoryPriceData();
                //历史财报数据每分钟一次
                pullHistoryFinanceData();
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                }
            } catch (Throwable e) {
                String msg = Helper.stack(e);
                errorRepository.save(new ErrorPo(msg));
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
            finance163PullService.pullPriceData();
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
            finance163PullService.pullPriceData();
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
        List<StockInfoPo> all = stockInfoRepository.findAll(Example.of(StockInfoPo.builder().priceComplete(0).build()));
        if(all.isEmpty()){
            return;
        }
        for (StockInfoPo stockInfoPo : all) {
            finance163PullService.pullHistoryPriceData(stockInfoPo);
        }
    }

    private void pullHistoryFinanceData(){
        //是否拉取完成
        List<StockInfoPo> all = stockInfoRepository.findAll(Example.of(StockInfoPo.builder().priceComplete(0).build()));
        if(all.isEmpty()){
            return;
        }
        for (StockInfoPo stockInfoPo : all) {
            finance163PullService.pullFinanceData(stockInfoPo);
        }
    }

    private void checkNewStock(){
        List<String> financeCodes=mongoTemplate.findDistinct("code", StockFinancePo.class,String.class);
        List<String> stockCodes=mongoTemplate.findDistinct("code", StockInfoPo.class,String.class);
        financeCodes.removeAll(stockCodes);
        if(financeCodes.isEmpty()){
            return;
        }
        for (String financeCode : financeCodes) {
            Example<StockFinancePo> example = Example.of(StockFinancePo.builder().code(financeCode).build());
            StockFinancePo po = stockFinanceRepository.findOne(example).get();
            finance163PullService.pullStockInfo(financeCode,po==null?"":po.getExchange());
        }
    }
}
