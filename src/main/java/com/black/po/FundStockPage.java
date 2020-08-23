package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class FundStockPage {
    String fundCode;
    String stockCode;
    BigDecimal stockNums;
    BigDecimal stockAmount;
    BigDecimal stockRatio;
    String date;

    public FundStockPage() {
    }
}