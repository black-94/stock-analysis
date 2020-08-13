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
import com.black.util.Helper;
import com.black.util.PoBuildUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
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
        List<StockFinancePo> list = stockHistoryFinanceRepository.queryByCode(e.getCode());
        list.sort(Comparator.comparing(StockFinancePo::getDate).reversed());
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            StockFinancePo cur = list.get(i);
            StockFinancePo pre = list.get(i + 1);

            BigDecimal m2mIncome = calculate(cur.getIncome(), pre.getIncome());
            BigDecimal m2mProfit = calculate(cur.getProfit(), pre.getProfit());

            if (cur.getM2mIncome() == null && m2mIncome != null) {
                stockHistoryFinanceRepository.updateField("m2mIncome", m2mIncome.toString(), cur.getId());
            }

            if (cur.getM2mProfit() == null && m2mProfit != null) {
                stockHistoryFinanceRepository.updateField("m2mProfit", m2mProfit.toString(), cur.getId());
            }
        }
        for (int i = 0; i < size - 1; i++) {
            StockFinancePo cur = list.get(i);
            Date lastYear = Helper.datePlus(cur.getDate(), -1, ChronoUnit.YEARS);
            StockFinancePo pre = list.stream().filter(p -> p.getDate().equals(lastYear)).findFirst().orElse(null);

            if (pre == null) {
                continue;
            }

            BigDecimal y2yIncome = calculate(cur.getIncome(), pre.getIncome());
            BigDecimal y2yProfit = calculate(cur.getProfit(), pre.getProfit());

            if (cur.getY2yIncome() == null && y2yIncome != null) {
                stockHistoryFinanceRepository.updateField("y2yIncome", y2yIncome.toString(), cur.getId());
            }

            if (cur.getY2yProfit() == null && y2yProfit != null) {
                stockHistoryFinanceRepository.updateField("y2yProfit", y2yProfit.toString(), cur.getId());
            }
        }
        for (int i = 0; i < size - 1; i++) {
            StockFinancePo cur = list.get(i);
            Date lastYear = Helper.datePlus(cur.getDate(), -1, ChronoUnit.YEARS);
            List<StockFinancePo> financeIn = list.subList(i, size).stream().filter(p -> p.getDate().after(lastYear)).collect(Collectors.toList());
            BigDecimal profitSum = financeIn.stream().map(StockFinancePo::getProfit).reduce(BigDecimal::add).orElse(BigDecimal.valueOf(-1));
            StockPricePo recentPrice = stockPriceRepository.queryOneBeforeDate(cur.getCode(), cur.getDate());
            if (recentPrice == null) {
                continue;
            }
            BigDecimal pe = Helper.safeDivide(recentPrice.getCapital(), profitSum);
            if (cur.getPe() == null && pe != null) {
                stockHistoryFinanceRepository.updateField("pe", pe.toString(), cur.getId());
            }
        }
    }

    private BigDecimal calculate(BigDecimal cur, BigDecimal pre) {
        if (cur == null || pre == null) {
            return null;
        }
        if (cur.compareTo(pre) >= 0) {
            if (pre.compareTo(BigDecimal.ZERO) < 0) {
                return Helper.safeDivide(cur, pre).abs().add(BigDecimal.ONE);
            }
            return Helper.safeDivide(cur, pre).abs().subtract(BigDecimal.ONE);
        }
        if (pre.compareTo(BigDecimal.ZERO) < 0) {
            return Helper.safeDivide(cur, pre).abs().negate().add(BigDecimal.ONE);
        }
        return Helper.safeDivide(cur, pre).abs().negate().subtract(BigDecimal.ONE);
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
