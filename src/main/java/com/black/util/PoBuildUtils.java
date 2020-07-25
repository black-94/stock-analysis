package com.black.util;

import com.black.po.*;
import org.springframework.beans.BeanUtils;

import static com.black.util.Helper.decimalOf;
import static com.black.util.Helper.parseDate;

public class PoBuildUtils {

    public static StockInfoPo buildStockInfo(Finance163StockInfoPO info) {
        StockInfoPo po=new StockInfoPo();
        BeanUtils.copyProperties(info,po);
        return po;
    }

    public static StockPricePo buildStockPrice(Finance163StockPricePO price) {
        StockPricePo po=new StockPricePo();
        po.setCode(price.getCode());
        po.setOpen(decimalOf(price.getOpen()));
        po.setClose(decimalOf(price.getCur()));
        po.setHigh(decimalOf(price.getHigh()));
        po.setLow(decimalOf(price.getLow()));
        po.setVolume(decimalOf(price.getVolume()));
        po.setAmount(decimalOf(price.getAmount()));
        po.setUpdown(decimalOf(price.getUpdown()));
        po.setChange(decimalOf(price.getChange()));
        po.setExchange(decimalOf(price.getExchange()));
        po.setCapital(decimalOf(price.getCapital()));
        po.setDate(parseDate(price.getDate()));
        return po;
    }

    public static StockHistoryPricePo buildStockHistoryPrice(Finance163StockHistoryPricePO price) {
        StockHistoryPricePo po=new StockHistoryPricePo();
        po.setCode(price.getCode());
        po.setOpen(decimalOf(price.getOpen()));
        po.setClose(decimalOf(price.getClose()));
        po.setHigh(decimalOf(price.getHigh()));
        po.setLow(decimalOf(price.getLow()));
        po.setVolume(decimalOf(price.getVolume()));
        po.setTurnover(decimalOf(price.getAmount()));
        po.setPercent(decimalOf(price.getUpdown()));
        po.setUpdown(decimalOf(price.getChange()));
        po.setAmplitude(decimalOf(price.getAmplitude()));
//        po.setExchange();
//        po.setCapital();
        po.setDate(parseDate(price.getDate()));
        return po;
    }

    public static StockFinancePo buildStockFinance(Finance163StockHistoryFinancePO financePO) {
        StockFinancePo po=new StockFinancePo();
        po.setCode(financePO.getCode());
        po.setIncome(decimalOf(financePO.getIncome()));
        po.setProfit(decimalOf(financePO.getProfit()));
        po.setDate(parseDate(financePO.getDate()));
        return po;
    }

    public static FundPricePO buildFundPrice(Finance163FundPricePO pricePO) {
//        StockFinancePo po=new StockFinancePo();
//        po.setCode(financePO.getCode());
//        po.setIncome(decimalOf(financePO.getIncome()));
//        po.setProfit(decimalOf(financePO.getProfit()));
//        po.setDate(parseDate(financePO.getDate()));
//        return po;
        return null;
    }

    public static FundStockPO buildFundStock(Finance163FundStockPO stockPO) {
//        StockFinancePo po=new StockFinancePo();
//        po.setCode(financePO.getCode());
//        po.setIncome(decimalOf(financePO.getIncome()));
//        po.setProfit(decimalOf(financePO.getProfit()));
//        po.setDate(parseDate(financePO.getDate()));
//        return po;
        return null;
    }
}
