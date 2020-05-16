package com.black.repository;

import com.black.po.Finance163StockHistoryPricePO;
import com.black.po.Finance163StockInfoPO;
import com.black.po.Finance163StockPricePO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Finance163Test {
    @Autowired
    Finance163Repository finance163Repository;

    @Test
    public void queryStockInfo(){
        Finance163StockInfoPO po = finance163Repository.queryInfo("600036", "sh");
        System.out.println(po);
    }

    @Test
    public void queryStockPrice(){
        Finance163StockPricePO po = finance163Repository.queryCurPrice("600036", "sh");
        System.out.println(po);
    }

    @Test
    public void queryStockHistoryPrice(){
        List<Finance163StockHistoryPricePO> pos = finance163Repository.queryHistoryPrice("600036", "sh");
        System.out.println(pos);
    }
}
