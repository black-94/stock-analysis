package com.black.jobs;

import com.black.po.IpoStockPage;
import com.black.po.StockDayPricePO;
import com.black.po.StockFinancePage;
import com.black.po.StockFundPage;
import com.black.po.StockInfoPO;
import com.black.po.StockInfoPage;
import com.black.po.StockNumPage;
import com.black.po.StockPriceHistoryPage;
import com.black.po.StockPricePage;
import com.black.po.StockQuartlyReportPO;
import com.black.repository.Finance163Repository;
import com.black.repository.IpoStockPageRepository;
import com.black.repository.StockDayPriceRepository;
import com.black.repository.StockFinancePageRepository;
import com.black.repository.StockFundPageRepository;
import com.black.repository.StockInfoPageRepository;
import com.black.repository.StockInfoRepository;
import com.black.repository.StockNumPageRepository;
import com.black.repository.StockPriceHistoryPageRepository;
import com.black.repository.StockPricePageRepository;
import com.black.repository.StockQuartlyReportRepository;
import com.black.util.Helper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.black.util.ExecutorUtil.submit;

@Component
public class Calculator {
    static Logger validateLogger = LogManager.getLogger("root.validate.log");

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
    @Autowired
    StockQuartlyReportRepository stockQuartlyReportRepository;
    @Autowired
    StockDayPriceRepository stockDayPriceRepository;

    @Scheduled(cron = "0 0 18 * * ? ")
    public void dayJob() {
        List<String> ipoCodes = ipoStockPageRepository.queryAllCodes();
        List<String> codes = stockInfoRepository.queryAllCodes();
        Collection<String> newCodes = CollectionUtils.removeAll(ipoCodes, codes);
        newCodes.forEach(e -> submit(() -> infoInit(e)));
        ipoCodes.forEach(e -> submit(() -> dayPriceInit(e, new Date())));
    }

    public void quartlyJob() {
        List<String> codes = stockInfoRepository.queryAllCodes();
        codes.forEach(e -> submit(() -> curQuartlyInit(e, new Date())));
    }

    public void allInit() {
        List<String> ipoCodes = ipoStockPageRepository.queryAllCodes();
        List<String> codes = stockInfoRepository.queryAllCodes();
        Collection<String> newCodes = CollectionUtils.removeAll(ipoCodes, codes);
        newCodes.forEach(e -> submit(() -> infoInit(e)));
        LocalDateTime now = LocalDateTime.now().plus(-750, ChronoUnit.DAYS);
        for (int i = 0; i < 750; i++) {
            Date datetime = Date.from(now.plus(i, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant());
            ipoCodes.forEach(e -> submit(() -> dayPriceInit(e, datetime)));
            ipoCodes.forEach(e -> submit(() -> curQuartlyInit(e, datetime)));
        }
    }

    public void infoInit(String code) {
        IpoStockPage ipoStockPage = ipoStockPageRepository.queryByCode(code);
        StockInfoPage stockInfoPage = stockInfoPageRepository.queryByCode(code);
        StockNumPage stockNumPage = stockNumPageRepository.queryRecent(code, 1).stream().findFirst().orElse(null);
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

    public void quartlyAllInit(){
        List<String> codes = ipoStockPageRepository.queryAllCodes();
        codes.forEach(e->submit(()->quartlyInit(e)));
    }

    public void quartlyInit(String code) {
        List<StockFinancePage> stockFinancePages = stockFinancePageRepository.queryByCode(code);
        if (CollectionUtils.isEmpty(stockFinancePages)) {
            validateLogger.info("quartlyInit finance is empty , code : " + code);
            return;
        }

        Collections.sort(stockFinancePages, Comparator.comparing(StockFinancePage::getReportDay));

        List<StockQuartlyReportPO> list = Lists.newArrayList();
        for (int i = 1; i < stockFinancePages.size(); i++) {
            StockFinancePage stockFinancePage = stockFinancePages.get(i);
            StockFinancePage lastFinance = stockFinancePages.get(i - 1);
            boolean yearBegin = !lastFinance.getReportDay().substring(0, 4).equals(stockFinancePage.getReportDay().substring(0, 4));

            StockQuartlyReportPO reportPO = new StockQuartlyReportPO();
            reportPO.setCode(stockFinancePage.getCode());
            reportPO.setReportDay(Helper.parseDate(stockFinancePage.getReportDay()));
            reportPO.setTotalIncome(Helper.decimalOf(stockFinancePage.getIncome()));
            reportPO.setTotalProfit(Helper.decimalOf(stockFinancePage.getProfit()));
            if (yearBegin) {
                BigDecimal lastProfit = Helper.decimalOf(lastFinance.getProfit());
                BigDecimal lastIncome = Helper.decimalOf(lastFinance.getIncome());
                if (!BigDecimal.ZERO.equals(lastIncome) && !BigDecimal.ZERO.equals(lastProfit)
                        && !BigDecimal.ZERO.equals(reportPO.getTotalIncome()) && !BigDecimal.ZERO.equals(reportPO.getTotalProfit())) {
                    reportPO.setIncome(reportPO.getTotalIncome().subtract(lastIncome));
                    reportPO.setProfit(reportPO.getTotalProfit().subtract(lastProfit));
                }
            } else {
                reportPO.setIncome(reportPO.getTotalIncome());
                reportPO.setProfit(reportPO.getTotalProfit());
            }

            list.add(reportPO);
        }

        StockFinancePage stockFinancePage = stockFinancePages.get(0);
        StockQuartlyReportPO firstReportPo = new StockQuartlyReportPO();
        firstReportPo.setCode(stockFinancePage.getCode());
        firstReportPo.setReportDay(Helper.parseDate(stockFinancePage.getReportDay()));
        firstReportPo.setTotalIncome(Helper.decimalOf(stockFinancePage.getIncome()));
        firstReportPo.setTotalProfit(Helper.decimalOf(stockFinancePage.getProfit()));
        firstReportPo.setIncome(firstReportPo.getTotalIncome());
        firstReportPo.setProfit(firstReportPo.getTotalProfit());
        list.add(firstReportPo);

        Collections.sort(list, Comparator.comparing(StockQuartlyReportPO::getReportDay));

        for (int i = 1; i < list.size(); i++) {
            StockQuartlyReportPO reportPO = list.get(i);
            StockQuartlyReportPO lastReport = list.get(i - 1);
            LocalDate localDate = reportPO.getReportDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate lastDate = lastReport.getReportDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            boolean isLast = localDate.plusMonths(-3).getMonthValue() == lastDate.getMonthValue();
            StockQuartlyReportPO lastYear = list.stream().filter(e -> e.getReportDay().equals(Date.from(localDate.plusYears(-1).atStartOfDay(ZoneId.systemDefault()).toInstant()))).findAny().orElse(null);

            if (isLast && lastReport.getIncome() != null && lastReport.getProfit() != null) {
                BigDecimal m2mIncome = Helper.calculateIncrease(reportPO.getIncome(), lastReport.getIncome());
                BigDecimal m2mProfit = Helper.calculateIncrease(reportPO.getProfit(), lastReport.getProfit());
                reportPO.setM2mIncome(m2mIncome);
                reportPO.setM2mProfit(m2mProfit);
            }
            if (lastYear != null && lastYear.getIncome() != null && lastYear.getProfit() != null) {
                BigDecimal y2yIncome = Helper.calculateIncrease(reportPO.getIncome(), lastYear.getIncome());
                BigDecimal y2yProfit = Helper.calculateIncrease(reportPO.getProfit(), lastYear.getProfit());
                reportPO.setY2yIncome(y2yIncome);
                reportPO.setY2yProfit(y2yProfit);
            }
        }

        stockQuartlyReportRepository.deleteByCode(code);
        stockQuartlyReportRepository.batchInsert(list);

    }


    public void curQuartlyInit(String code, Date datetime) {
        String reportDay = Helper.formatDate(Helper.findRecentReportDay(datetime));
        List<StockFinancePage> financePages = stockFinancePageRepository.queryRecent(code, 2);
        StockFinancePage curFinance = financePages.stream().filter(e -> e.getReportDay().equals(reportDay)).findFirst().orElse(null);
        if (curFinance == null) {
            return;
        }
        StockFinancePage lastFinance = financePages.get(financePages.size() - 1);
        BigDecimal curIncome = Helper.decimalOf(curFinance.getIncome());
        BigDecimal curProfit = Helper.decimalOf(curFinance.getProfit());
        BigDecimal lastIncome = Helper.decimalOf(lastFinance.getIncome());
        BigDecimal lastProfit = Helper.decimalOf(lastFinance.getProfit());

        StockQuartlyReportPO reportPO = new StockQuartlyReportPO();
        reportPO.setTotalIncome(curIncome);
        reportPO.setTotalProfit(curProfit);
        reportPO.setReportDay(Helper.parseDate(reportDay));
        if (curFinance.getReportDay().substring(0, 4).equals(lastFinance.getReportDay().substring(0, 4))) {
            reportPO.setIncome(curIncome.subtract(lastIncome));
            reportPO.setProfit(curProfit.subtract(lastProfit));
        } else {
            reportPO.setIncome(curIncome);
            reportPO.setProfit(curProfit);
        }

        List<StockFundPage> stockFundPages = stockFundPageRepository.queryByCodeAndDate(code, reportDay);
        StockNumPage stockNumPage = stockNumPageRepository.queryRecentBefore(code, 1, reportDay).stream().findFirst().orElse(null);
        if (CollectionUtils.isNotEmpty(stockFundPages) && stockNumPage != null) {
            BigDecimal fundStocks = stockFundPages.stream().map(e -> e.getStockNums()).map(Helper::decimalOf).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            BigDecimal ratio = Helper.safeDivide(fundStocks, Helper.decimalOf(stockNumPage.getTotal()));
            reportPO.setFundRatio(ratio);
        }

        Date reportDate = Helper.parseDate(reportDay);
        Date lastMonth = Helper.datePlus(reportDate, -3, ChronoUnit.MONTHS);
        Date lastYear = Helper.datePlus(reportDate, -1, ChronoUnit.YEARS);
        StockQuartlyReportPO lastMonthReport = stockQuartlyReportRepository.queryByCodeAndDate(code, lastMonth);
        StockQuartlyReportPO lastYearReport = stockQuartlyReportRepository.queryByCodeAndDate(code, lastYear);

        if (lastMonthReport != null) {
            reportPO.setM2mIncome(Helper.calculateIncrease(reportPO.getIncome(), lastMonthReport.getIncome()));
            reportPO.setM2mProfit(Helper.calculateIncrease(reportPO.getProfit(), lastMonthReport.getProfit()));
        }
        if (lastYearReport != null) {
            reportPO.setY2yIncome(Helper.calculateIncrease(reportPO.getIncome(), lastYearReport.getIncome()));
            reportPO.setY2yProfit(Helper.calculateIncrease(reportPO.getProfit(), lastYearReport.getProfit()));
        }

        stockQuartlyReportRepository.insert(reportPO);
    }

    public void dayPriceInit(String code, Date datetime) {
        String now = Helper.formatDate(datetime);
        StockPricePage curPrice = stockPricePageRepository.queryByCodeAndDate(code, now);
        StockPriceHistoryPage curHistoryPrice = stockPriceHistoryPageRepository.queryByCodeAndDate(code, now);
        StockNumPage stockNumPage = stockNumPageRepository.queryRecentBefore(code, 1, now).stream().findFirst().orElse(null);
        Date end = new Date();
        Date begin = Helper.datePlus(end, -1, ChronoUnit.YEARS);
        List<StockQuartlyReportPO> stockQuartlyReportPOS = stockQuartlyReportRepository.queryBetween(code, begin, end);
        StockDayPricePO lastStockDayPrice = stockDayPriceRepository.recentBefore(code, now, 1);

        if (curPrice == null && curHistoryPrice == null) {
            return;
        }

        BigDecimal lastClose = null;
        BigDecimal open = null;
        BigDecimal close = null;
        BigDecimal high = null;
        BigDecimal low = null;
        BigDecimal volume = null;
        BigDecimal amount = null;
        BigDecimal percent = null;
        BigDecimal change = null;
        BigDecimal amplitude = null;
        BigDecimal exchange = null;
        BigDecimal total = null;
        BigDecimal capital = null;
        BigDecimal pe = null;
        Date date = Helper.parseDate(now);

        if (curPrice != null) {
            lastClose = Helper.decimalOf(curPrice.getLastClose());
            open = Helper.decimalOf(curPrice.getOpen());
            close = Helper.decimalOf(curPrice.getCur());
            high = Helper.decimalOf(curPrice.getHigh());
            low = Helper.decimalOf(curPrice.getLow());
            volume = Helper.decimalOf(curPrice.getVolume());
            amount = Helper.decimalOf(curPrice.getAmount());
            percent = Helper.decimalOf(curPrice.getPercent());
            change = Helper.decimalOf(curPrice.getChange());
        }
        if (lastClose == null && lastStockDayPrice != null) {
            lastClose = lastStockDayPrice.getClose();
        }
        if (open == null) {
            open = Helper.decimalOf(curHistoryPrice.getOpen());
        }
        if (close == null) {
            close = Helper.decimalOf(curHistoryPrice.getClose());
        }
        if (high == null) {
            high = Helper.decimalOf(curHistoryPrice.getHigh());
        }
        if (low == null) {
            low = Helper.decimalOf(curHistoryPrice.getLow());
        }
        if (volume == null) {
            volume = Helper.decimalOf(curHistoryPrice.getVolume());
        }
        if (amount == null) {
            amount = Helper.decimalOf(curHistoryPrice.getAmount());
        }
        if (percent == null) {
            percent = Helper.decimalOf(curHistoryPrice.getPercent());
        }
        if (change == null) {
            change = Helper.decimalOf(curHistoryPrice.getChange());
        }
        if (amplitude == null) {
            if (curHistoryPrice == null) {
                amplitude = Helper.safeDivide(high.subtract(low), lastClose);
            } else {
                amplitude = Helper.decimalOf(curHistoryPrice.getAmplitude());
            }
        }
        if (exchange == null) {
            exchange = Helper.decimalOf(curHistoryPrice.getExchange());
        }
        if (total == null && stockNumPage != null) {
            total = Helper.decimalOf(stockNumPage.getTotal());
        }
        if (capital == null && total != null) {
            capital = total.multiply(close);
        }
        if (pe == null && capital != null) {
            BigDecimal profit = stockQuartlyReportPOS.stream().map(e -> e.getProfit()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            pe = Helper.safeDivide(capital, profit);
        }

        StockDayPricePO stockDayPricePO = new StockDayPricePO();
        stockDayPricePO.setCode(code);
        stockDayPricePO.setLastClose(lastClose);
        stockDayPricePO.setOpen(open);
        stockDayPricePO.setClose(close);
        stockDayPricePO.setHigh(high);
        stockDayPricePO.setLow(low);
        stockDayPricePO.setVolume(volume);
        stockDayPricePO.setAmount(amount);
        stockDayPricePO.setPercent(percent);
        stockDayPricePO.setChange(change);
        stockDayPricePO.setAmplitude(amplitude);
        stockDayPricePO.setExchange(exchange);
        stockDayPricePO.setTotal(total);
        stockDayPricePO.setCapital(capital);
        stockDayPricePO.setPe(pe);
        stockDayPricePO.setDate(date);

        stockDayPriceRepository.insert(stockDayPricePO);
    }


}
