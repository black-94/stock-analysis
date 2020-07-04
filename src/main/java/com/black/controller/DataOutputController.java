package com.black.controller;

import com.black.po.StockExchangerPo;
import com.black.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class DataOutputController {

    @GetMapping("/test")
    public String test(){
        return "中文测试";
    }

    @GetMapping("/seinit")
    public String init(){
        String[][] stockExchanges={
                {"NASDAQ","纳斯达克","usa"},
                {"NYSE","纽约证券交易所","usa"},
                {"HKEX","香港证券交易所","hk"},
                {"SSE","上海证券交易所","china"},
                {"SZSE","深圳证券交易所","china"}
        };
        List<StockExchangerPo> list = Arrays.stream(stockExchanges).map(e -> new StockExchangerPo(e[0], e[1], e[2])).collect(Collectors.toList());
        return "ok";
    }

}
