package com.black.repository;

import com.black.po.Finance163FundPricePO;
import com.black.po.Finance163FundStockPO;
import com.black.po.Finance163StockHistoryFinancePO;
import com.black.po.Finance163StockHistoryPricePO;
import com.black.po.Finance163StockInfoPO;
import com.black.po.Finance163StockPricePO;
import com.black.po.StockInfoPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class Finance163Test {
    @Autowired
    Finance163Repository finance163Repository;

//    @Test
    public void queryAllCodes(){
        List<StockInfoPo> stockInfoPos = finance163Repository.queryAllCodes();
        System.out.println(stockInfoPos);
    }

//    @Test
    public void queryTodayStockCodes(){
        List<StockInfoPo> stockInfoPos = finance163Repository.queryTodayCodes();
        System.out.println(stockInfoPos);
    }

//    @Test
    public void queryStockInfo(){
        Finance163StockInfoPO po = finance163Repository.queryInfo("600036");
        System.out.println(po);
    }

//    @Test
    public void queryStockPrice(){
        Finance163StockPricePO po = finance163Repository.queryCurPrice("600036", "sh");
        System.out.println(po);
    }

//    @Test
    public void queryStockHistoryPrice() throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date marketDay = format.parse("2010-01-01");
        List<Finance163StockHistoryPricePO> pos = finance163Repository.queryHistoryPrice("600036", marketDay);
        System.out.println(pos);
    }

//    @Test
    public void queryStockHistoryFinance(){
        List<Finance163StockHistoryFinancePO> pos = finance163Repository.queryHistoryFinance("600036");
        System.out.println(pos);
    }

//    @Test
    public void fundTest(){
        List<Finance163FundPricePO> funds = finance163Repository.fundList();
        System.out.println(funds);
        List<Finance163FundStockPO> stockPOS = finance163Repository.fundStockList("320007");
        System.out.println(stockPOS);
        List<Finance163FundStockPO> historyStocks = finance163Repository.fundHistoryStock("320007");
        System.out.println(historyStocks);
        List<Finance163FundPricePO> pricePOS = finance163Repository.fundHistoryPrice("320007", "2020-01-01");
        System.out.println(pricePOS);
    }

}
