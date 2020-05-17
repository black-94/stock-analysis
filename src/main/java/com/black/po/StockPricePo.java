package com.black.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockPricePo {
    long id;
    String code;
    BigDecimal open;
    BigDecimal cur;
    BigDecimal lastClose;
    BigDecimal high;
    BigDecimal low;
    BigDecimal volume;
    BigDecimal amount;
    BigDecimal updown;
    BigDecimal change;
    BigDecimal amplitude;
    Date date;
    Date createTime;
    Date updateTime;
}