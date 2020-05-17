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
        po.setCur(decimalOf(price.getCur()));
        po.setLastClose(decimalOf(price.getLastClose()));
        po.setHigh(decimalOf(price.getHigh()));
        po.setLow(decimalOf(price.getLow()));
        po.setVolume(decimalOf(price.getVolume()));
        po.setAmount(decimalOf(price.getAmount()));
        po.setUpdown(decimalOf(price.getUpdown()));
        po.setChange(decimalOf(price.getChange()));
        po.setAmplitude(decimalOf(price.getAmplitude()));
        po.setDate(parseDate(price.getDate()));
        return po;
    }

    public static StockHistoryPricePo buildStockHistoryPrice(Finance163StockHistoryPricePO price) {
        return null;
    }

    public static StockFinancePo buildStockFinance(Finance163StockHistoryFinancePO financePO) {
        return null;
    }
}
