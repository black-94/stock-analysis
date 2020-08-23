package com.black.controller;

import com.black.jobs.Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class DataOutputController {

    @Autowired
    Crawler crawler;

    @GetMapping("/test")
    public String test() {
        return "中文测试";
    }

    @GetMapping("/seinit")
    public String init() {
        String[][] stockExchanges = {
                {"NASDAQ", "纳斯达克", "usa"},
                {"NYSE", "纽约证券交易所", "usa"},
                {"HKEX", "香港证券交易所", "hk"},
                {"SSE", "上海证券交易所", "china"},
                {"SZSE", "深圳证券交易所", "china"}
        };
        return "ok";
    }

    @GetMapping("/job/{job}")
    public String invokeJob(@PathVariable String job) throws Exception {
        Method method = crawler.getClass().getMethod(job);
        method.invoke(crawler);
        return "ok";
    }

}
