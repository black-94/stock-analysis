package com.black.util;

import com.black.po.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

import static com.black.util.Helper.decimalOf;
import static com.black.util.Helper.parseDate;
import static com.black.util.Helper.parseTextNumber;
import static com.black.util.Helper.safeDivide;

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
        po.setPercent(decimalOf(price.getUpdown()));
        po.setUpdown(decimalOf(price.getChange()));
        po.setTotal(parseTextNumber(price.getTotal()));
        po.setNum(parseTextNumber(price.getNum()));

        BigDecimal amplitude=safeDivide(po.getHigh().subtract(po.getLow()),decimalOf(price.getLastClose()));
        po.setAmplitude(amplitude);

        BigDecimal capital=po.getTotal().multiply(po.getClose());
        po.setCapital(capital);

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
        po.setAmount(decimalOf(price.getAmount()));
        po.setPercent(decimalOf(price.getUpdown()));
        po.setUpdown(decimalOf(price.getChange()));
        po.setAmplitude(decimalOf(price.getAmplitude()));

        BigDecimal exchange=decimalOf(price.getExchange());
        BigDecimal total=safeDivide(po.getAmount().multiply(BigDecimal.valueOf(100*100)),exchange);
        BigDecimal capital=safeDivide(po.getVolume().multiply(BigDecimal.valueOf(10000*100)),exchange);

        po.setTotal(total);
        po.setCapital(capital);
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
        FundPricePO po=new FundPricePO();
//        po.setFundCode(pricePO.getFundCode());
//        po.setUnit(pricePO.getUnit());
//        po.setRatio(pricePO.getRatio());
//        po.setAmount(pricePO.getAmount());
//        po.setM1ret(pricePO.getM1ret());
//        po.setM3ret(pricePO.getM3ret());
//        po.setM6ret(pricePO.getM6ret());
//        po.setM12ret(pricePO.getM12ret());
//        po.setDate(pricePO.getDate());
        return po;
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
