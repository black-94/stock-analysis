package com.black.jobs;

import com.black.po.IpoStockPage;
import com.black.po.StockFinancePage;
import com.black.po.StockFundPage;
import com.black.po.StockInfoPO;
import com.black.po.StockInfoPage;
import com.black.po.StockNumPage;
import com.black.po.StockQuartlyReportPO;
import com.black.repository.Finance163Repository;
import com.black.repository.IpoStockPageRepository;
import com.black.repository.StockFinancePageRepository;
import com.black.repository.StockFundPageRepository;
import com.black.repository.StockInfoPageRepository;
import com.black.repository.StockInfoRepository;
import com.black.repository.StockNumPageRepository;
import com.black.repository.StockPriceHistoryPageRepository;
import com.black.repository.StockPricePageRepository;
import com.black.util.Helper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.black.util.ExecutorUtil.submit;

@Component
public class Calculator {
    @Autowired
    Finance163Repository finance163Repository;
    @Autowired
    IpoStockPageRepository ipoStockPageRepository;
    @Autowired
    StockInfoPageRepository stockInfoPageRepository;
    @Autowired
    StockNumPageRepository stockNumPageRepository;
    @Autowired
    StockFinancePageRepository stockFinancePageRepository;
    @Autowired
    StockPricePageRepository stockPricePageRepository;
    @Autowired
    StockPriceHistoryPageRepository stockPriceHistoryPageRepository;
    @Autowired
    StockFundPageRepository stockFundPageRepository;
    @Autowired
    StockInfoRepository stockInfoRepository;

    public void dayJob() {
        List<String> ipoCodes = ipoStockPageRepository.queryAllCodes();
        List<String> codes = stockInfoRepository.queryAllCodes();
        Collection<String> newCodes = CollectionUtils.removeAll(ipoCodes, codes);
        newCodes.forEach(e -> submit(() -> infoInit(e)));
        codes.forEach(e -> submit(() -> quartlyInit(e)));
        codes.forEach(e -> submit(() -> dayPriceInit(e)));
    }

    public void infoInit(String code) {
        IpoStockPage ipoStockPage = ipoStockPageRepository.queryByCode(code);
        StockInfoPage stockInfoPage = stockInfoPageRepository.queryByCode(code);
        StockNumPage stockNumPage = stockNumPageRepository.queryByCode(code);
        String name = StringUtils.isBlank(ipoStockPage.getName()) ? stockInfoPage.getName() : ipoStockPage.getName();
        String biz = StringUtils.isBlank(stockInfoPage.getBiz()) ? stockNumPage.getBiz() : stockInfoPage.getBiz();
        String marketDay = StringUtils.isBlank(ipoStockPage.getMarketDay()) ? stockNumPage.getMarketDay() : ipoStockPage.getMarketDay();

        StockInfoPO stockInfoPO = new StockInfoPO();
        stockInfoPO.setCode(code);
        stockInfoPO.setName(name);
        stockInfoPO.setBiz(biz);
        stockInfoPO.setOpenDay(Helper.parseDate(stockInfoPage.getOpenDay()));
        stockInfoPO.setMarketDay(Helper.parseDate(marketDay));

        stockInfoRepository.insert(stockInfoPO);
    }

    public void quartlyInit(String code) {
        StockFinancePage stockFinancePage = stockFinancePageRepository.recentOne(code);



    }

    public void dayPriceInit(String code) {
        Date endDate=Helper.findRecentReportDay();
        Date beginDate = Helper.datePlus(endDate, -1, ChronoUnit.YEARS);
        StockNumPage stockNumPage = stockNumPageRepository.recentOneAfter(new Date());
        List<StockFundPage> stockFundPages = stockFundPageRepository.queryByCodeAndDate(code, Helper.formatDate(endDate));
        List<StockFinancePageRepository> financeList = stockFinancePageRepository.queryBetween(code, beginDate, endDate);

        StockQuartlyReportPO quartlyReportPO=new StockQuartlyReportPO();

    }
}
