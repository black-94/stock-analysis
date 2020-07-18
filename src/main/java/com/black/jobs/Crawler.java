package com.black.jobs;

import com.black.po.*;
import com.black.repository.*;
import com.black.util.PoBuildUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class Crawler {
    public static Logger root = LoggerFactory.getLogger(Crawler.class);
    @Autowired
    Finance163Repository finance163Repository;
    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    StockPriceRepository stockPriceRepository;
    @Autowired
    StockHistoryFinanceRepository stockHistoryFinanceRepository;
    @Autowired
    StockHistoryPriceRepository stockHistoryPriceRepository;

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 1, TimeUnit.SECONDS,
            new ArrayBlockingQueue(1000), (r, e) -> Crawler.waitQueue(e, r));

    private static void waitQueue(ThreadPoolExecutor e, Runnable r) {
        try {
            e.getQueue().put(r);
        } catch (Exception exception) {
            root.error("", exception);
        }
    }

    private void waitComplete() {
        while (executor.getQueue().size() > 0) {
            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
                root.error("", e);
            }
        }
    }

    @Scheduled(cron = "0 0 16 * * ?")
    public void pullStockCodes() {
        List<String> codes = stockInfoRepository.queryAllCodes();
        List<StockInfoPo> stockInfoPos = finance163Repository.queryTodayCodes();
        List<StockInfoPo> list = stockInfoPos.stream().filter(e -> !codes.contains(e.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(list)) {
            stockInfoRepository.batchInsert(list);
        }
        initStockInfo();
        waitComplete();
    }

    public void initStockInfo() {
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryUninitStock();
        stockInfoPos.stream().forEach(e -> executor.submit(() -> this.singleFillInfo(e)));
    }

    private void singleFillInfo(StockInfoPo e) {
        Finance163StockInfoPO info = finance163Repository.queryInfo(e.getCode(), e.getExchanger());
        StockInfoPo stockInfoPo = PoBuildUtils.buildStockInfo(info);
        stockInfoRepository.fillInfo(stockInfoPo);
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void pullStockPrice() {
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos.stream().forEach(e -> executor.submit(() -> this.singleFillPrice(e)));
        waitComplete();
    }

    private void singleFillPrice(StockInfoPo e) {
        Finance163StockPricePO price = finance163Repository.queryCurPrice(e.getCode(), e.getExchanger());
        StockPricePo stockPricePo = PoBuildUtils.buildStockPrice(price);
        stockPriceRepository.insert(stockPricePo);
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void fillHistoryPrice() {
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos = stockInfoPos.stream().filter(e -> e.getPriceComplete() == 0).collect(Collectors.toList());
        stockInfoPos.stream().forEach(e -> executor.submit(() -> this.singleFillHistoryPrice(e)));
        waitComplete();
    }

    private void singleFillHistoryPrice(StockInfoPo e) {
        List<Finance163StockHistoryPricePO> prices = finance163Repository.queryHistoryPrice(e.getCode(), e.getExchanger(), e.getMarketDay());
        List<StockHistoryPricePo> list = prices.stream().map(PoBuildUtils::buildStockHistoryPrice).collect(Collectors.toList());
        List<Date> dates = stockHistoryPriceRepository.queryDatesByCode(e.getCode());
        list = list.stream().filter(p -> !dates.contains(p.getDate())).collect(Collectors.toList());
        if (list.isEmpty()) {
            return;
        }
        stockHistoryPriceRepository.batchInsert(list);
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void pullStockFinance() {
        //do nothing
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void fillHistoryFinance() {
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos = stockInfoPos.stream().filter(e -> e.getFinanceComplete() == 0).collect(Collectors.toList());
        stockInfoPos.stream().forEach(e -> executor.submit(() -> this.singleFillHistoryFinance(e)));
        waitComplete();
    }

    private void singleFillHistoryFinance(StockInfoPo e) {
        List<Finance163StockHistoryFinancePO> finances = finance163Repository.queryHistoryFinance(e.getCode(), e.getExchanger());
        List<StockFinancePo> stockFinancePos = finances.stream().map(PoBuildUtils::buildStockFinance).collect(Collectors.toList());
        List<Date> dates = stockHistoryFinanceRepository.queryDateByCode(e.getCode());
        stockFinancePos = stockFinancePos.stream().filter(shfp -> !dates.contains(shfp.getDate())).collect(Collectors.toList());
        if (stockFinancePos.isEmpty()) {
            return;
        }
        stockHistoryFinanceRepository.batchInsert(stockFinancePos);
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void fillFinanceCalRes() {
        //使用股价*流通计算涨跌

    }

}
