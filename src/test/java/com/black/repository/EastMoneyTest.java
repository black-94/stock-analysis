package com.black.repository;

import com.black.po.StockInfoPo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EastMoneyTest {
    @Autowired
    EastMoneyRepository eastMoneyRepository;

    @Test
    public void queryAllStocks(){
        List<StockInfoPo> stockInfoPos = eastMoneyRepository.queryAllStockCode();
        System.out.println(stockInfoPos);
    }
}
