package com.black.jobs;

import com.black.po.Finance163FundPricePO;
import com.black.po.Finance163FundStockPO;
import com.black.po.Finance163StockHistoryFinancePO;
import com.black.po.Finance163StockHistoryPricePO;
import com.black.po.Finance163StockInfoPO;
import com.black.po.Finance163StockPricePO;
import com.black.po.FundInfoPO;
import com.black.po.FundPricePO;
import com.black.po.FundStockPO;
import com.black.po.StockFinancePo;
import com.black.po.StockHistoryPricePo;
import com.black.po.StockInfoPo;
import com.black.po.StockPricePo;
import com.black.repository.Finance163Repository;
import com.black.repository.FundInfoRepository;
import com.black.repository.FundPriceRepository;
import com.black.repository.FundStockRepository;
import com.black.repository.StockHistoryFinanceRepository;
import com.black.repository.StockHistoryPriceRepository;
import com.black.repository.StockInfoRepository;
import com.black.repository.StockPriceRepository;
import com.black.util.PoBuildUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
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
        Finance163StockInfoPO info = finance163Repository.queryInfo(e.getCode());
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
        StockPricePo tmp = stockPriceRepository.queryByDate(stockPricePo.getCode(), stockPricePo.getDate());
        if (tmp != null) {
            return;
        }
        stockPriceRepository.insert(stockPricePo);
        if (new Date(0).equals(e.getMarketDay())) {
            stockInfoRepository.updateField("marketDay", price.getMarketDay(), e.getId());
        }
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void fillHistoryPrice() {
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos = stockInfoPos.stream().filter(e -> e.getPriceComplete() == 0).collect(Collectors.toList());
        stockInfoPos.stream().forEach(e -> submit(() -> this.singleFillHistoryPrice(e)));
        waitComplete();
    }

    private void singleFillHistoryPrice(StockInfoPo e) {
        Date endDate = e.getPriceComplete() > 0 ? Date.from(LocalDate.now().plusMonths(-1).atStartOfDay(ZoneId.systemDefault()).toInstant()) : e.getMarketDay();
        List<Finance163StockHistoryPricePO> prices = finance163Repository.queryHistoryPrice(e.getCode(), endDate);
        List<StockHistoryPricePo> list = prices.stream().map(PoBuildUtils::buildStockHistoryPrice).collect(Collectors.toList());
        List<Date> dates = stockHistoryPriceRepository.queryDatesByCode(e.getCode());
        list = list.stream().filter(p -> !dates.contains(p.getDate())).collect(Collectors.toList());
        if (!list.isEmpty()) {
            stockHistoryPriceRepository.batchInsert(list);
        }
        if (e.getPriceComplete() <= 0) {
            stockInfoRepository.updateField("priceComplete", "1", e.getId());
        }
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void fillHistoryFinance() {
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos = stockInfoPos.stream().filter(e -> e.getFinanceComplete() == 0).collect(Collectors.toList());
        stockInfoPos.stream().forEach(e -> submit(() -> this.singleFillHistoryFinance(e)));
        waitComplete();
    }

    private void singleFillHistoryFinance(StockInfoPo e) {
        List<Finance163StockHistoryFinancePO> finances = finance163Repository.queryHistoryFinance(e.getCode());
        List<StockFinancePo> stockFinancePos = finances.stream().map(PoBuildUtils::buildStockFinance).collect(Collectors.toList());
        List<Date> dates = stockHistoryFinanceRepository.queryDateByCode(e.getCode());
        stockFinancePos = stockFinancePos.stream().filter(shfp -> !dates.contains(shfp.getDate())).collect(Collectors.toList());
        if (!stockFinancePos.isEmpty()) {
            stockHistoryFinanceRepository.batchInsert(stockFinancePos);
        }
        fillFinanceCalRes(e);
        if (e.getFinanceComplete() <= 1) {
            stockInfoRepository.updateField("financeComplete", "1", e.getId());
        }
    }

    private void fillFinanceCalRes(StockInfoPo e) {
        stockHistoryFinanceRepository.q



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
            List<Finance163FundPricePO> prices = finance163Repository.fundHistoryPrice(e.getFundCode(), e.getMarketDate());
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
