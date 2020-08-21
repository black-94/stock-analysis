package com.black.repository;

import com.black.po.*;
import com.black.pojo.Finance163FundPricePO;
import com.black.pojo.Finance163FundStockPO;
import com.black.pojo.Finance163StockHistoryFinancePO;
import com.black.pojo.Finance163StockHistoryPricePO;
import com.black.pojo.Finance163StockInfoPO;
import com.black.pojo.Finance163StockPricePO;
import com.black.pojo.StockInfoPo;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
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

    @Test
    public void stockTest(){
        List<IpoStockPage> ipoStockPages = finance163Repository.queryCodes("2020");
        String code = ipoStockPages.get(RandomUtils.nextInt(0, ipoStockPages.size())).getCode();
        StockInfoPage stockInfoPage = finance163Repository.queryInfov2(code);
        StockNumPage stockNumPage = finance163Repository.queryStockNum(code);
        List<StockFinancePage> stockFinancePages = finance163Repository.queryFinance(code);
        StockPricePage stockPricePage = finance163Repository.queryPriceV2(code);
        List<StockPriceHistoryPage> stockPriceHistoryPages = finance163Repository.queryHistoryPrice(code, "2020","0");
        System.out.println("success");
    }
}
