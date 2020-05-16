package com.black.repository;

import com.black.po.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EastMoneyTest {
    @Autowired
    EastMoneyRepository eastMoneyRepository;

    @Test
    public void queryStockInfo(){
        List<StockInfoPo> stockInfoPos = eastMoneyRepository.queryAllStockCode();
        System.out.println(stockInfoPos);
    }
}
