package com.black.repository;

import com.black.po.*;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Finance163Test {
    @Autowired
    Finance163Repository finance163Repository;

    //@Test
    public void fundTest() {
        List<FundPricePage> funds = finance163Repository.fundPrice();
        System.out.println(funds);
        List<FundStockPage> stockPOS = finance163Repository.fundStockList("320007", false);
        System.out.println(stockPOS);
        List<FundStockPage> historyStocks = finance163Repository.fundStockList("320007", false);
        System.out.println(historyStocks);
        List<FundPriceHistoryPage> pricePOS = finance163Repository.fundHistoryPrice("320007", "2020-01-01");
        System.out.println(pricePOS);
    }

    //@Test
    public void stockTest() {
        List<IpoStockPage> ipoStockPages = finance163Repository.queryCodes("2020");
        String code = ipoStockPages.get(RandomUtils.nextInt(0, ipoStockPages.size())).getCode();
        StockInfoPage stockInfoPage = finance163Repository.queryInfo(code);
        StockNumPage stockNumPage = finance163Repository.queryStockNum(code);
        List<StockFinancePage> stockFinancePages = finance163Repository.queryFinance(code);
        StockPricePage stockPricePage = finance163Repository.queryPrice(code);
        List<StockPriceHistoryPage> stockPriceHistoryPages = finance163Repository.queryHistoryPrice(code, "2020", "3");
        List<StockFundPage> stockFundPages = finance163Repository.queryStockFundPage("002475", true);
        System.out.println("success");
    }
}
