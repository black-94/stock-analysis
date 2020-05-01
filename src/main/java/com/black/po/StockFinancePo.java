package com.black.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class StockFinancePo {
    @Id
    String id;
    String code;
    String name;
    String exchange;
    Long date;
    String tag;
    BigDecimal income;
    BigDecimal y2yIncome;
    BigDecimal m2mIncome;
    BigDecimal profit;
    BigDecimal y2yProfit;
    BigDecimal m2mProfit;
    Long createTime;
    Long updateTime;
}
