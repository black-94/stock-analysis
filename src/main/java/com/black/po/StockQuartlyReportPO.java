package com.black.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockQuartlyReportPO {
    public StockQuartlyReportPO() {
    }

    String code;
    BigDecimal income;
    BigDecimal profit;
    BigDecimal totalIncome;
    BigDecimal totalProfit;
    BigDecimal fundRatio;
    BigDecimal m2mIncome;
    BigDecimal m2mProfit;
    BigDecimal y2yIncome;
    BigDecimal y2yProfit;
    Date reportDay;
}
