package com.black.controller;

import com.alibaba.fastjson.JSON;
import com.black.bean.BaseParam;
import com.black.bean.BaseResp;
import com.black.bean.StockFinancePullParam;
import com.black.bean.StockPricePullParam;
import com.black.constant.Constants;
import com.black.po.*;
import com.black.repository.*;
import com.google.common.collect.ImmutableMap;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
public class DataOutputController {

    private WebClient webClient=WebClient.create("http://api.waditu.com");
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
    @Autowired
    RequestRepository requestRepository;

    @GetMapping("/hello")
    public Mono<String> helloworld(){
        return Mono.just("helloworld");
    }

    @GetMapping("/gettest")
    public Mono<BaseResp> getDataTest(){
        BaseParam param=new BaseParam();
        param.setApi_name("stock_basic");
        param.setFields("");
        param.setToken(Constants.token);
        param.setParams(ImmutableMap.of("list_status","L"));
        Mono<BaseResp> resp = webClient.post().uri("").body(Mono.just(param), BaseParam.class).retrieve().bodyToMono(BaseResp.class);
        return resp;
    }

    @GetMapping("/init")
    public Flux<SEPo> init(){
        String[][] stockExchanges={
                {"NASDAQ","纳斯达克","usa"},
                {"NYSE","纽约证券交易所","usa"},
                {"HKEX","香港证券交易所","hk"},
                {"SSE","上海证券交易所","china"},
                {"SZSE","深圳证券交易所","china"}
        };
        List<SEPo> list = Arrays.stream(stockExchanges).map(e -> new SEPo(e[0], e[1], e[2])).collect(Collectors.toList());
        list.forEach(e-> seRepository.save(e));
        taskRepository.save(new TaskPo());
        stockInfoRepository.save(new StockInfoPo());
        stockPriceRepository.save(new StockPricePo());
        stockFinanceRepository.save(new StockFinancePo());
        requestRepository.save(new RequestPo());
        taskRepository.deleteAll();
        stockInfoRepository.deleteAll();
        stockPriceRepository.deleteAll();
        stockFinanceRepository.deleteAll();
        requestRepository.deleteAll();
        return Flux.just(list.toArray(new SEPo[0]));
    }

    @GetMapping("/addjob")
    public Flux<TaskPo> createTask(){
        StockPricePullParam pricePullParam=new StockPricePullParam();
        pricePullParam.setExchange(Constants.CHINA_EXCHANGE);

        TaskPo pricePullTask=new TaskPo();
        pricePullTask.setType(Constants.PRICE_PULL);
        pricePullTask.setParams(JSON.toJSONString(pricePullParam));
        pricePullTask.setScheduleTime(System.currentTimeMillis());
        pricePullTask.setCreateTime(System.currentTimeMillis());
        pricePullTask.setUpdateTime(System.currentTimeMillis());
        pricePullTask.setStatus(0);

        StockFinancePullParam financePullParam=new StockFinancePullParam();
        pricePullParam.setExchange(Constants.CHINA_EXCHANGE);

        TaskPo financePullTask=new TaskPo();
        financePullTask.setType(Constants.FINANCE_PULL);
        financePullTask.setParams(JSON.toJSONString(financePullParam));
        financePullTask.setScheduleTime(System.currentTimeMillis());
        financePullTask.setCreateTime(System.currentTimeMillis());
        financePullTask.setUpdateTime(System.currentTimeMillis());
        financePullTask.setStatus(0);

        taskRepository.save(pricePullTask);
        taskRepository.save(financePullTask);

        PageRequest pageRequest=PageRequest.of(0,10, Sort.by("scheduleTime").descending());
        Page<TaskPo> page = taskRepository.findAll(pageRequest);
        List<TaskPo> tasks = page.getContent();
        return Flux.just(tasks.toArray(new TaskPo[0]));
    }

}
