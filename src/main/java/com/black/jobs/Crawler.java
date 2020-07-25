package com.black.jobs;

import com.black.po.*;
import com.black.repository.*;
import com.black.util.PoBuildUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.black.util.ExecutorUtil.submit;
import static com.black.util.ExecutorUtil.waitComplete;

@Component
public class Crawler {
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
    @Autowired
    FundInfoRepository fundInfoRepository;
    @Autowired
    FundPriceRepository fundPriceRepository;
    @Autowired
    FundStockRepository fundStockRepository;

    public void pullAllStockCodes() {
        List<String> codes = stockInfoRepository.queryAllCodes();
        List<StockInfoPo> stockInfoPos = finance163Repository.queryAllCodes();
        List<StockInfoPo> list = stockInfoPos.stream().filter(e -> !codes.contains(e.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(list)) {
            stockInfoRepository.batchInsert(list);
        }
        initStockInfo();
        waitComplete();
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
        stockInfoPos.stream().forEach(e -> submit(() -> this.singleFillInfo(e)));
    }

    private void singleFillInfo(StockInfoPo e) {
        Finance163StockInfoPO info = finance163Repository.queryInfo(e.getCode(), e.getExchanger());
        StockInfoPo stockInfoPo = PoBuildUtils.buildStockInfo(info);
        stockInfoRepository.fillInfo(stockInfoPo);
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void pullStockPrice() {
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos.stream().forEach(e -> submit(() -> this.singleFillPrice(e)));
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
        stockInfoPos.stream().forEach(e -> submit(() -> this.singleFillHistoryPrice(e)));
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
        stockInfoPos.stream().forEach(e -> submit(() -> this.singleFillHistoryFinance(e)));
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

    @Scheduled(cron = "0 0 23 * * ?")
    public void pullFundPrices() {
        List<Finance163FundPricePO> pos = finance163Repository.fundList();
        List<FundPricePO> list = pos.stream().map(PoBuildUtils::buildFundPrice).collect(Collectors.toList());
        list.forEach(fundPricePO -> submit(() -> {
            FundPricePO po = fundPriceRepository.queryByDate(fundPricePO.getFundCode(), fundPricePO.getDate());
            if (po == null) {
                fundPriceRepository.insert(fundPricePO);
            }
        }));
    }

    @Scheduled(cron = "0 0 0 1 1/3 ?")
    public void pullFundStock() {
        List<String> codes = fundInfoRepository.queryAllCodes();
        codes.forEach(e -> submit(() -> {
            List<Finance163FundStockPO> pos = finance163Repository.fundStockList(e);
            List<FundStockPO> list = pos.stream().map(PoBuildUtils::buildFundStock).collect(Collectors.toList());
            fundStockRepository.batchInsert(list);
        }));
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void fillFundInfo() {
        List<FundInfoPO> funds = fundInfoRepository.queryUninitFund();
        funds.forEach(e -> submit(() -> {
            List<Finance163FundPricePO> prices = finance163Repository.fundHistoryPrice(e.getFundCode(),e.getMarketDate());
            List<FundPricePO> priceList = prices.stream().map(PoBuildUtils::buildFundPrice).collect(Collectors.toList());
            for (FundPricePO fundPricePO : priceList) {
                FundPricePO tmp = fundPriceRepository.queryByDate(fundPricePO.getFundCode(), fundPricePO.getDate());
                if (tmp == null) {
                    fundPriceRepository.insert(fundPricePO);
                }
            }
            List<Finance163FundStockPO> stocks = finance163Repository.fundHistoryStock(e.getFundCode());
            List<FundStockPO> stockList = stocks.stream().map(PoBuildUtils::buildFundStock).collect(Collectors.toList());
            for (FundStockPO fundStockPO : stockList) {
                FundStockPO tmp = fundStockRepository.queryByDate(fundStockPO.getFundCode(), fundStockPO.getStockCode(), fundStockPO.getDate());
                if (tmp == null) {
                    fundStockRepository.insert(fundStockPO);
                }
            }
        }));
    }
}
