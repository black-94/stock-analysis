package com.black.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockPricePo {
    long id;
    String code;
    BigDecimal lastClose;
    BigDecimal open;
    BigDecimal close;
    BigDecimal high;
    BigDecimal low;
    BigDecimal volume;
    BigDecimal amount;
    BigDecimal percent;
    BigDecimal updown;
    BigDecimal amplitude;
    BigDecimal total;
    BigDecimal num;
    BigDecimal capital;
    Date date;
    Date createTime;
    Date updateTime;
}