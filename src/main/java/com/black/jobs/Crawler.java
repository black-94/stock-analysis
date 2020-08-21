package com.black.jobs;

import com.black.enums.Constant;
import com.black.po.IpoStockPage;
import com.black.po.StockFinancePage;
import com.black.po.StockInfoPage;
import com.black.po.StockNumPage;
import com.black.po.StockPricePage;
import com.black.pojo.Finance163FundPricePO;
import com.black.pojo.Finance163FundStockPO;
import com.black.pojo.Finance163StockHistoryFinancePO;
import com.black.pojo.Finance163StockHistoryPricePO;
import com.black.pojo.Finance163StockInfoPO;
import com.black.pojo.Finance163StockPricePO;
import com.black.pojo.FundInfoPO;
import com.black.pojo.FundPricePO;
import com.black.pojo.FundStockPO;
import com.black.pojo.StockFinancePo;
import com.black.pojo.StockHistoryPricePo;
import com.black.pojo.StockInfoPo;
import com.black.pojo.StockPricePo;
import com.black.repository.Finance163Repository;
import com.black.repository.FundInfoRepository;
import com.black.repository.FundPriceRepository;
import com.black.repository.FundStockRepository;
import com.black.repository.IpoStockPageRepository;
import com.black.repository.StockFinancePageRepository;
import com.black.repository.StockHistoryFinanceRepository;
import com.black.repository.StockHistoryPriceRepository;
import com.black.repository.StockInfoPageRepository;
import com.black.repository.StockInfoRepository;
import com.black.repository.StockNumPageRepository;
import com.black.repository.StockPricePageRepository;
import com.black.repository.StockPriceRepository;
import com.black.util.ExecutorUtil;
import com.black.util.FailContext;
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
import java.util.Collections;
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
    IpoStockPageRepository ipoStockPageRepository;
    @Autowired
    StockInfoPageRepository stockInfoPageRepository;
    @Autowired
    StockNumPageRepository stockNumPageRepository;
    @Autowired
    StockFinancePageRepository stockFinancePageRepository;
    @Autowired
    StockPricePageRepository stockPricePageRepository;

    public void firstInit() {
        initCodes();
        initStockInfos();
        initStockNums();
        initStockFinances();
        initStockPrices();
        initStockPriceHistorys();
    }

    public void initCodes() {
        ipoStockPageRepository.deleteAll();
        int beginYear = Constant.CHINA_IPO_START_YEAR;
        int endYear = LocalDate.now().getYear();
        for (int i = beginYear; i <= endYear; i++) {
            List<IpoStockPage> ipoStockPages = finance163Repository.queryCodes(String.valueOf(i));
            if (ipoStockPages.isEmpty()) {
                continue;
            }
            ipoStockPageRepository.batchInsert(ipoStockPages);
        }
    }

    public void initStockInfos() {
        stockInfoPageRepository.deleteAll();
        List<String> codes = ipoStockPageRepository.queryAllCodes();
        codes.forEach(e -> submit(() -> initStockInfo(e)));
    }

    public void initStockInfo(String code) {
        stockInfoPageRepository.deleteByCode(code);
        StockInfoPage stockInfoPage = finance163Repository.queryInfov2(code);
        stockInfoPageRepository.insert(stockInfoPage);
    }

    public void initStockNums() {
        stockNumPageRepository.deleteAll();
        List<String> codes = ipoStockPageRepository.queryAllCodes();
        codes.forEach(e -> submit(() -> initStockNum(e)));
    }

    public void initStockNum(String code) {
        String date = Helper.formatDate(new Date());
        stockNumPageRepository.deleteByCode(code, date);
        StockNumPage stockNumPage = finance163Repository.queryStockNum(code);
        stockNumPageRepository.insert(stockNumPage);
    }

    public void initStockFinances() {
        stockFinancePageRepository.deleteAll();
        List<String> codes = ipoStockPageRepository.queryAllCodes();
        codes.forEach(e -> submit(() -> initStockFinance(e)));
    }

    public void initStockFinance(String code) {
        stockFinancePageRepository.deleteByCode(code);
        List<StockFinancePage> stockFinancePages = finance163Repository.queryFinance(code);
        stockFinancePageRepository.batchInsert(stockFinancePages);
    }

    public void initStockPrices() {
        stockPricePageRepository.deleteAll();
        List<String> codes = ipoStockPageRepository.queryAllCodes();
        codes.forEach(e -> submit(() -> initStockPrice(e)));

    }

    public void initStockPrice(String code) {
        String date = Helper.formatDate(new Date());
        stockPricePageRepository.deleteByCode(code, date);
        StockPricePage stockPricePage = finance163Repository.queryPriceV2(code);
        stockPricePageRepository.insert(stockPricePage);
    }

    public void initStockPriceHistorys() {

    }

    public void initStockPriceHistory(String code) {

    }


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
        FailContext.put("singleFillInfo", e);
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
        FailContext.put("singleFillPrice", e);
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

    @Scheduled(cron = "0 0 22 * * ?")
    public void fillHistoryPrice() {
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos.stream().forEach(e -> submit(() -> this.singleFillHistoryPrice(e)));
        waitComplete();
    }

    private void singleFillHistoryPrice(StockInfoPo e) {
        FailContext.put("singleFillHistoryPrice", e);
        Date endDate = e.getPriceComplete() > 0 ? Date.from(LocalDate.now().plusMonths(-1).atStartOfDay(ZoneId.systemDefault()).toInstant()) : e.getMarketDay();
        List<Finance163StockHistoryPricePO> prices = finance163Repository.queryHistoryPrice(e.getCode(), endDate);
        List<StockHistoryPricePo> list = prices.stream().map(PoBuildUtils::buildStockHistoryPrice).collect(Collectors.toList());
        List<Date> dates = stockHistoryPriceRepository.queryDatesByCode(e.getCode());
        list = list.stream().filter(p -> !dates.contains(p.getDate())).collect(Collectors.toList());
        if (!list.isEmpty()) {
            List<StockFinancePo> stockFinancePos = stockHistoryFinanceRepository.queryByCode(e.getCode());
            fillPE(stockFinancePos, list);
            stockHistoryPriceRepository.batchInsert(list);
        }
        if (e.getPriceComplete() <= 0) {
            stockInfoRepository.updateField("priceComplete", "1", e.getId());
        }
    }

    private void fillPE(List<StockFinancePo> finances, List<StockHistoryPricePo> prices) {
        Collections.sort(finances, Comparator.comparing(StockFinancePo::getDate).reversed());
        for (StockHistoryPricePo price : prices) {
            Date cur = price.getDate();
            Date lastYear = Helper.datePlus(cur, -1, ChronoUnit.YEARS);
            List<StockFinancePo> financeIn = finances.stream().filter(e -> e.getDate().before(cur)).filter(e -> e.getDate().after(lastYear)).collect(Collectors.toList());
            BigDecimal profitSum = financeIn.stream().map(StockFinancePo::getProfit).reduce(BigDecimal::add).orElse(BigDecimal.valueOf(-1));
            BigDecimal pe = Helper.safeDivide(price.getCapital().multiply(BigDecimal.valueOf(4)), profitSum.multiply(BigDecimal.valueOf(financeIn.size())));
            price.setPe(pe);
        }
    }

    @Scheduled(cron = "0 0 19 * * ?")
    public void fillHistoryFinance() {
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos.stream().forEach(e -> submit(() -> this.singleFillHistoryFinance(e)));
        waitComplete();
    }

    private void singleFillHistoryFinance(StockInfoPo e) {
        FailContext.put("singleFillHistoryFinance", e);
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

    @Scheduled(cron = "0 0/10 * * * ?")
    public void retry() {
        FailContext.addFail(FailContext.gap);
        while (FailContext.hasNext()) {
            FailContext.FailObject fail = FailContext.getFail();
            if (fail == FailContext.gap) {
                return;
            }
            ExecutorUtil.submit(() -> {
                switch (fail.getType()) {
                    case "singleFillInfo":
                        singleFillInfo((StockInfoPo) fail.getParam());
                        break;
                    case "singleFillPrice":
                        singleFillPrice((StockInfoPo) fail.getParam());
                        break;
                    case "singleFillHistoryPrice":
                        singleFillHistoryPrice((StockInfoPo) fail.getParam());
                        break;
                    case "singleFillHistoryFinance":
                        singleFillHistoryFinance((StockInfoPo) fail.getParam());
                        break;
                    default:
                        break;
                }
            });
        }
    }
}
