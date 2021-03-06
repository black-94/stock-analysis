package com.black.jobs;

import com.black.po.IpoStockPage;
import com.black.po.MarketBreakPO;
import com.black.po.StockDayPricePO;
import com.black.po.StockFinancePage;
import com.black.po.StockInfoPO;
import com.black.po.StockInfoPage;
import com.black.po.StockNumPage;
import com.black.po.StockPriceHistoryPage;
import com.black.po.StockPricePage;
import com.black.repository.IpoStockPageRepository;
import com.black.repository.MarketBreakRepository;
import com.black.repository.StockDayPriceRepository;
import com.black.repository.StockFinancePageRepository;
import com.black.repository.StockInfoPageRepository;
import com.black.repository.StockInfoRepository;
import com.black.repository.StockNumPageRepository;
import com.black.repository.StockPriceHistoryPageRepository;
import com.black.repository.StockPricePageRepository;
import com.black.util.Helper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.black.util.ExecutorUtil.submit;

@Component
public class Validator {
    static Logger validateLogger = LogManager.getLogger("root.validate.log");

    @Autowired
    MarketBreakRepository marketBreakRepository;
    @Autowired
    StockPricePageRepository stockPricePageRepository;
    @Autowired
    StockDayPriceRepository stockDayPriceRepository;
    @Autowired
    StockInfoPageRepository stockInfoPageRepository;
    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    IpoStockPageRepository ipoStockPageRepository;
    @Autowired
    StockNumPageRepository stockNumPageRepository;
    @Autowired
    StockPriceHistoryPageRepository stockPriceHistoryPageRepository;
    @Autowired
    StockFinancePageRepository stockFinancePageRepository;

    public void validateAll() {
        validateIpoStockPage();
        validateStockInfoPage();
        validateStockInfo();
        Instant begin = LocalDate.now().plusYears(-5).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = LocalDate.now().plus(1, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant();
        validatePriceIn(Date.from(begin), Date.from(end));
    }

    @Scheduled(cron = "0 0 19 * * ?")
    public void validateDay() {
        validateIpoStockPage();
        validateStockInfoPage();
        validateStockInfo();
        Instant begin = LocalDate.now().plus(-1, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        validatePriceIn(Date.from(begin), Date.from(end));
    }

    public void validatePriceIn(Date begin, Date end) {
        List<MarketBreakPO> marketBreakPOS = marketBreakRepository.queryByTimezone("Asia/Shanghai", begin, end);
        List<Date> breakDates = marketBreakPOS.stream().map(MarketBreakPO::getBreakDate).collect(Collectors.toList());
        for (Date tmp = begin; tmp.before(end); tmp = Helper.datePlus(tmp, 1, ChronoUnit.DAYS)) {
            int weekday = tmp.toInstant().atZone(ZoneId.systemDefault()).getDayOfWeek().getValue();
            if (weekday == 6 || weekday == 7 || breakDates.contains(tmp)) {
                continue;
            }
            final Date date = tmp;
            submit(() -> validatePricePage(date));
            submit(() -> validateStockPriceHistoryPage(date));
            submit(() -> validateStockNumPage(date));
        }
        for (Date tmp = end; tmp.after(begin); tmp = Helper.datePlus(tmp, -3, ChronoUnit.MONTHS)) {
            final Date date = tmp;
            submit(() -> validateStockFinancePage(date));
        }
    }

    public void validatePricePage(Date date) {
        List<StockPricePage> stockPricePages = stockPricePageRepository.queryByDate(Helper.formatDate(date));
        if (CollectionUtils.isEmpty(stockPricePages)) {
            validateLogger.info(String.format("validatePricePage empty , date : %s", Helper.formatDate(date)));
        }
        List<String> incorrectCodes = stockPricePages.stream().filter(this::validatePricePage).map(StockPricePage::getCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(incorrectCodes)) {
            return;
        }
        validateLogger.info(String.format("validatePricePage illegal : %s , date : %s", incorrectCodes, Helper.formatDate(date)));
    }

    private boolean validatePricePage(StockPricePage stockPricePage) {
        if (StringUtils.isBlank(stockPricePage.getLastClose())) {
            return true;
        }
        if (StringUtils.isBlank(stockPricePage.getCur())) {
            return true;
        }
        if (new BigDecimal(stockPricePage.getPercent()).compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        return false;
    }

    public void validatePrice(Date date) {
        List<StockDayPricePO> stockDayPricePOS = stockDayPriceRepository.queryByDate(Helper.formatDate(date));
        if (CollectionUtils.isEmpty(stockDayPricePOS)) {
            validateLogger.info(String.format("validatePrice empty , date : %s", Helper.formatDate(date)));
        }
        List<String> incorrectCodes = stockDayPricePOS.stream().filter(this::validatePrice).map(StockDayPricePO::getCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(incorrectCodes)) {
            return;
        }
        validateLogger.info(String.format("validatePrice illegal : %s , date : %s", incorrectCodes, Helper.formatDate(date)));
    }

    private boolean validatePrice(StockDayPricePO stockDayPricePO) {
        if (stockDayPricePO.getLastClose() == null || stockDayPricePO.getLastClose().compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        if (stockDayPricePO.getClose() == null || stockDayPricePO.getClose().compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        if (stockDayPricePO.getPercent() == null || stockDayPricePO.getPercent().compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        if (stockDayPricePO.getPe() == null || stockDayPricePO.getPe().compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        return false;
    }

    public void validateIpoStockPage() {
        List<IpoStockPage> ipoStockPages = ipoStockPageRepository.queryAll();
        List<String> codes = ipoStockPages.stream().filter(this::validateIpoStockPage).map(IpoStockPage::getCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(codes)) {
            return;
        }
        validateLogger.info(String.format(" ipo stock page codes : %s ", codes));
    }

    private boolean validateIpoStockPage(IpoStockPage ipoStockPage) {
        if (StringUtils.isBlank(ipoStockPage.getCode())) {
            return true;
        }
        if (StringUtils.isBlank(ipoStockPage.getName())) {
            return true;
        }
        if (StringUtils.isBlank(ipoStockPage.getMarketYear())) {
            return true;
        }
        if (StringUtils.isBlank(ipoStockPage.getMarketDay())) {
            return true;
        }
        return false;
    }

    public void validateStockInfoPage() {
        List<StockInfoPage> stockInfoPages = stockInfoPageRepository.queryAll();
        List<String> codes = stockInfoPages.stream().filter(this::validateStockInfoPage).map(StockInfoPage::getCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(codes)) {
            return;
        }
        validateLogger.info(String.format(" stock info page codes : %s ", codes));
    }

    private boolean validateStockInfoPage(StockInfoPage stockInfoPage) {
        if (StringUtils.isBlank(stockInfoPage.getCode())) {
            return true;
        }
        if (StringUtils.isBlank(stockInfoPage.getName())) {
            return true;
        }
        if (StringUtils.isBlank(stockInfoPage.getBiz())) {
            return true;
        }
        if (StringUtils.isBlank(stockInfoPage.getMarketDay())) {
            return true;
        }
        if (StringUtils.isBlank(stockInfoPage.getOpenDay())) {
            return true;
        }
        return false;
    }

    public void validateStockInfo() {
        List<StockInfoPO> stockInfoPOS = stockInfoRepository.queryAll();
        List<String> codes = stockInfoPOS.stream().filter(this::validateStockInfo).map(StockInfoPO::getCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(codes)) {
            return;
        }
        validateLogger.info(String.format(" stock info codes : %s ", codes));
    }

    private boolean validateStockInfo(StockInfoPO stockInfoPO) {
        if (StringUtils.isBlank(stockInfoPO.getCode())) {
            return true;
        }
        if (StringUtils.isBlank(stockInfoPO.getName())) {
            return true;
        }
        if (StringUtils.isBlank(stockInfoPO.getBiz())) {
            return true;
        }
        if (stockInfoPO.getMarketDay() == null) {
            return true;
        }
        if (stockInfoPO.getOpenDay() == null) {
            return true;
        }
        return false;
    }

    public void validateStockNumPage(Date date) {
        List<StockNumPage> stockNumPages = stockNumPageRepository.queryByDate(Helper.formatDate(date));
        if (CollectionUtils.isEmpty(stockNumPages)) {
            validateLogger.info(String.format("validateStockNumPage empty , date : %s", Helper.formatDate(date)));
            return;
        }
        List<String> codes = stockNumPages.stream().filter(this::validateStockNumPage).map(StockNumPage::getCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(codes)) {
            return;
        }
        validateLogger.info(String.format("validateStockNumPage illegal : %s , date : %s", codes, Helper.formatDate(date)));
    }

    private boolean validateStockNumPage(StockNumPage stockNumPage) {
        if (StringUtils.isBlank(stockNumPage.getCode())) {
            return true;
        }
        if (StringUtils.isBlank(stockNumPage.getName())) {
            return true;
        }
        if (StringUtils.isBlank(stockNumPage.getBiz())) {
            return true;
        }
        if (StringUtils.isBlank(stockNumPage.getTotal())) {
            return true;
        }
        if (StringUtils.isBlank(stockNumPage.getCycle())) {
            return true;
        }
        if (StringUtils.isBlank(stockNumPage.getMarketDay())) {
            return true;
        }
        return false;
    }

    public void validateStockPriceHistoryPage(Date date) {
        List<StockPriceHistoryPage> stockPriceHistoryPages = stockPriceHistoryPageRepository.queryByDate(Helper.formatDate(date));
        if (CollectionUtils.isEmpty(stockPriceHistoryPages)) {
            validateLogger.info(String.format("validateStockPriceHistoryPage empty , date : %s", Helper.formatDate(date)));
            return;
        }
        List<String> codes = stockPriceHistoryPages.stream().filter(this::validateStockPriceHistoryPage).map(StockPriceHistoryPage::getCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(codes)) {
            return;
        }
        validateLogger.info(String.format("validateStockPriceHistoryPage illegal : %s , date : %s", codes, Helper.formatDate(date)));
    }

    private boolean validateStockPriceHistoryPage(StockPriceHistoryPage stockPriceHistoryPage) {
        if (StringUtils.isBlank(stockPriceHistoryPage.getCode())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getOpen())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getHigh())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getLow())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getClose())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getPercent())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getChange())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getAmplitude())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getVolume())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getAmount())) {
            return true;
        }
        if (StringUtils.isBlank(stockPriceHistoryPage.getExchange())) {
            return true;
        }
        return false;
    }

    public void validateStockFinancePage(Date date) {
        date = Helper.datePlus(date, -3, ChronoUnit.MONTHS);
        List<StockFinancePage> stockFinancePages = stockFinancePageRepository.queryAfterDate(date);
        if (CollectionUtils.isEmpty(stockFinancePages)) {
            validateLogger.info(String.format("validateStockFinancePage empty , date : %s", Helper.formatDate(date)));
            return;
        }
        Set<String> codeSet = stockFinancePages.stream().map(StockFinancePage::getCode).collect(Collectors.toSet());
        List<String> allCodes = ipoStockPageRepository.queryAllCodes();
        Set<String> notInCodes = allCodes.stream().filter(code -> !codeSet.contains(code)).collect(Collectors.toSet());
        validateLogger.info(String.format("validateStockFinancePage lack finance , codes : %s , date : %s", notInCodes, Helper.formatDate(date)));
        List<String> codes = stockFinancePages.stream().filter(this::validateStockFinancePage).map(StockFinancePage::getCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(codes)) {
            return;
        }
        validateLogger.info(String.format("validateStockFinancePage illegal : %s , date : %s", codes, Helper.formatDate(date)));
    }

    private boolean validateStockFinancePage(StockFinancePage stockFinancePage) {
        if (StringUtils.isBlank(stockFinancePage.getCode())) {
            return true;
        }
        if (StringUtils.isBlank(stockFinancePage.getIncome())) {
            return true;
        }
        if (StringUtils.isBlank(stockFinancePage.getProfit())) {
            return true;
        }
        if (StringUtils.isBlank(stockFinancePage.getReportDay())) {
            return true;
        }
        return false;
    }

}
