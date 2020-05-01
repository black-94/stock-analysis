package com.black.controller;

import com.black.bean.BaseParam;
import com.black.bean.BaseResp;
import com.black.constant.Constants;
import com.black.po.SEPo;
import com.black.repository.SERepository;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
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
        return Flux.just(seRepository.findAll().toArray(new SEPo[0]));
    }





}
