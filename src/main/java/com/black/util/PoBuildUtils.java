package com.black.util;

import com.black.po.Finance163StockHistoryFinancePO;
import com.black.po.Finance163StockHistoryPricePO;
import com.black.po.Finance163StockInfoPO;
import com.black.po.Finance163StockPricePO;
import com.black.po.StockFinancePo;
import com.black.po.StockHistoryPricePo;
import com.black.po.StockInfoPo;
import com.black.po.StockPricePo;
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
        StockHistoryPricePo po=new StockHistoryPricePo();
        po.setCode(price.getCode());
        po.setExchange(price.getExchange());
        po.setOpen(decimalOf(price.getOpen()));
        po.setLastClose(decimalOf(price.getClose()));
        po.setHigh(decimalOf(price.getHigh()));
        po.setLow(decimalOf(price.getLow()));
        po.setVolumn(decimalOf(price.getVolume()));
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
}
