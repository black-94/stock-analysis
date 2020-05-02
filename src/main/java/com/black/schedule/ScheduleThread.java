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
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
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
                errorRepository.save(ErrorPo.builder().type(e.getClass().getName()).msg(e.getMessage()).stack(Helper.stack(e)).build());
            }
        }
    }

    private void pullPriceData(){
        //有未完成任务吗，没有就拉
        Long endTime=System.currentTimeMillis();
        Long hour16=Date.from(LocalTime.of(16, 0).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()).getTime();
        TaskPo recentTask = taskRepository.findRecentTasks(Constants.PRICE_PULL);
        if(recentTask!=null&&recentTask.getStatus()==0){
            eastMoneyPullService.pullFinanceData();
            return;
        }
        //四点之后有拉过数据吗，没有就拉
        TaskPo completeTask = taskRepository.findCompleteTasks(Constants.PRICE_PULL);
        if(completeTask!=null&&completeTask.getScheduleCompleteTime().getTime()>hour16){
            return;
        }
        //现在过了四点吗
        if(endTime>hour16){
            finance163PullService.pullPriceData();
            TaskPo po=new TaskPo();
            po.setType(Constants.PRICE_PULL);
            po.setStatus(1);
            po.setScheduleTime(new Date());
            po.setScheduleCompleteTime(new Date());
            po.setCreateTime(new Date());
            po.setUpdateTime(new Date());
            taskRepository.insert(po);
            return;
        }
    }

    private void pullFinanceData(){
        //有未完成任务吗，没有就拉
        Long endTime=System.currentTimeMillis();
        Long hour16=Date.from(LocalTime.of(16, 0).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()).getTime();
        TaskPo recentTask = taskRepository.findRecentTasks(Constants.FINANCE_PULL);
        if(recentTask!=null&&recentTask.getStatus()==0){
            eastMoneyPullService.pullFinanceData();
            return;
        }
        //四点之后有拉过数据吗，没有就拉
        TaskPo completeTask = taskRepository.findCompleteTasks(Constants.FINANCE_PULL);
        if(completeTask!=null&&completeTask.getScheduleCompleteTime().getTime()>hour16){
            return;
        }
        //现在过了四点吗
        if(endTime>hour16){
            eastMoneyPullService.pullFinanceData();
            TaskPo po=new TaskPo();
            po.setType(Constants.FINANCE_PULL);
            po.setStatus(1);
            po.setScheduleTime(new Date());
            po.setScheduleCompleteTime(new Date());
            po.setCreateTime(new Date());
            po.setUpdateTime(new Date());
            taskRepository.insert(po);
            return;
        }
    }

    private void pullHistoryPriceData(){
        //是否拉取完成
        List<StockInfoPo> all = stockInfoRepository.queryByStatus("priceComplete");
        if(all.isEmpty()){
            return;
        }

        all.parallelStream().forEach(finance163PullService::pullHistoryPriceData);
    }

    private void pullHistoryFinanceData(){
        //是否拉取完成
        List<StockInfoPo> all = stockInfoRepository.queryByStatus("financeComplete");
        if(all.isEmpty()){
            return;
        }

        all.parallelStream().forEach(finance163PullService::pullFinanceData);
    }

    private void checkNewStock(){
        List<String> financeCodes=stockFinanceRepository.queryCodes();
        List<String> stockCodes=stockInfoRepository.queryCodes();
        financeCodes.removeAll(stockCodes);
        if(financeCodes.isEmpty()){
            return;
        }

        financeCodes.parallelStream().forEach(e->{
            StockFinancePo po = stockFinanceRepository.findByCode(e);
            finance163PullService.pullStockInfo(e,po==null?"":po.getExchange());
        });
    }
}
