package com.black.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockDayPricePO {
    public StockDayPricePO() {
    }

    String code;
    BigDecimal lastClose;
    BigDecimal open;
    BigDecimal close;
    BigDecimal high;
    BigDecimal low;
    BigDecimal volume;
    BigDecimal amount;
    BigDecimal percent;
    BigDecimal change;
    BigDecimal amplitude;
    BigDecimal exchange;
    BigDecimal total;
    BigDecimal capital;
    BigDecimal pe;
    Date date;
}
