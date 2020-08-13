package com.black.util;

import com.black.po.Finance163FundPricePO;
import com.black.po.Finance163FundStockPO;
import com.black.po.Finance163StockHistoryFinancePO;
import com.black.po.Finance163StockHistoryPricePO;
import com.black.po.Finance163StockInfoPO;
import com.black.po.Finance163StockPricePO;
import com.black.po.FundPricePO;
import com.black.po.FundStockPO;
import com.black.po.StockFinancePo;
import com.black.po.StockHistoryPricePo;
import com.black.po.StockInfoPo;
import com.black.po.StockPricePo;

import java.math.BigDecimal;

import static com.black.util.Helper.decimalOf;
import static com.black.util.Helper.parseDate;
import static com.black.util.Helper.parseTextNumber;
import static com.black.util.Helper.safeDivide;

public class PoBuildUtils {

    public static StockInfoPo buildStockInfo(Finance163StockInfoPO info) {
        StockInfoPo po=new StockInfoPo();
        po.setCode(info.getCode());
        po.setName(info.getName());
        po.setBiz(info.getBiz());
        po.setOpenDay(parseDate(info.getOpenDay()));
        po.setMarketDay(parseDate(info.getMarketDay()));
        return po;
    }

    public static StockPricePo buildStockPrice(Finance163StockPricePO price) {
        StockPricePo po=new StockPricePo();
        po.setCode(price.getCode());
        po.setLastClose(decimalOf(price.getLastClose()));
        po.setOpen(decimalOf(price.getOpen()));
        po.setClose(decimalOf(price.getCur()));
        po.setHigh(decimalOf(price.getHigh()));
        po.setLow(decimalOf(price.getLow()));
        po.setVolume(decimalOf(price.getVolume()));
        po.setAmount(decimalOf(price.getAmount()));
        po.setPercent(decimalOf(price.getUpdown()).multiply(BigDecimal.valueOf(100)));
        po.setUpdown(decimalOf(price.getChange()));
        po.setTotal(parseTextNumber(price.getTotal()));
        po.setNum(parseTextNumber(price.getNum()));

        BigDecimal amplitude=safeDivide(po.getHigh().subtract(po.getLow()).multiply(BigDecimal.valueOf(100)),decimalOf(price.getLastClose()));
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
        po.setVolume(decimalOf(price.getVolume()).multiply(BigDecimal.valueOf(100)));
        po.setAmount(decimalOf(price.getAmount()).multiply(BigDecimal.valueOf(10000)));
        po.setPercent(decimalOf(price.getUpdown()));
        po.setUpdown(decimalOf(price.getChange()));
        po.setAmplitude(decimalOf(price.getAmplitude()));

        BigDecimal exchange=decimalOf(price.getExchange());
        BigDecimal capital=safeDivide(po.getAmount().multiply(BigDecimal.valueOf(100)),exchange);
        BigDecimal total=safeDivide(po.getVolume().multiply(BigDecimal.valueOf(100)),exchange);
        BigDecimal lastClose=po.getClose().subtract(po.getUpdown());

        po.setTotal(total);
        po.setCapital(capital);
        po.setLastClose(lastClose);
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
