package com.black.controller;

import com.black.bean.StockFinancePullParam;
import com.black.bean.StockPricePullParam;
import com.black.constant.Constants;
import com.black.po.SEPo;
import com.black.po.TaskPo;
import com.black.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
public class DataOutputController {

    @Autowired
    SERepository seRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    StockPriceRepository stockPriceRepository;
    @Autowired
    StockFinanceRepository stockFinanceRepository;

    @GetMapping("/init")
    public String init(){
        String[][] stockExchanges={
                {"NASDAQ","纳斯达克","usa"},
                {"NYSE","纽约证券交易所","usa"},
                {"HKEX","香港证券交易所","hk"},
                {"SSE","上海证券交易所","china"},
                {"SZSE","深圳证券交易所","china"}
        };
        List<SEPo> list = Arrays.stream(stockExchanges).map(e -> new SEPo(e[0], e[1], e[2])).collect(Collectors.toList());
        seRepository.insert(list);
        return "ok";
    }

    @GetMapping("/addjob")
    public String createTask(){
        Date now=new Date();

        StockPricePullParam pricePullParam=new StockPricePullParam();
        pricePullParam.setExchange(Constants.CHINA_EXCHANGE);

        TaskPo pricePullTask=new TaskPo();
        pricePullTask.setType(Constants.PRICE_PULL);
        pricePullTask.setScheduleTime(now);
        pricePullTask.setCreateTime(now);
        pricePullTask.setUpdateTime(now);
        pricePullTask.setStatus(0);

        StockFinancePullParam financePullParam=new StockFinancePullParam();
        financePullParam.setExchange(Constants.CHINA_EXCHANGE);

        TaskPo financePullTask=new TaskPo();
        financePullTask.setType(Constants.FINANCE_PULL);
        financePullTask.setScheduleTime(now);
        financePullTask.setCreateTime(now);
        financePullTask.setUpdateTime(now);
        financePullTask.setStatus(0);

        taskRepository.insert(pricePullTask);
        taskRepository.insert(financePullTask);

        return "ok";
    }

}
