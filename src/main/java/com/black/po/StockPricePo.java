package com.black.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockPricePo {
    long id;
    String code;
    String name;
    String exchange;
    BigDecimal open;
    BigDecimal cur;
    BigDecimal lastClose;
    BigDecimal high;
    BigDecimal low;
    BigDecimal volumn;
    BigDecimal turnover;
    BigDecimal percent;
    Date date;
    Date createTime;
    Date updateTime;
}
