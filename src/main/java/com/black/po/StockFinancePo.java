package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@AllArgsConstructor
@Data
public class StockFinancePo {
    String id;
    String code;
    String name;
    String exchange;
    Date date;
    BigDecimal income;
    BigDecimal y2yIncome;
    BigDecimal m2mIncome;
    BigDecimal profit;
    BigDecimal y2yProfit;
    BigDecimal m2mProfit;
    Date createTime;
    Date updateTime;

    public StockFinancePo(){}
}
