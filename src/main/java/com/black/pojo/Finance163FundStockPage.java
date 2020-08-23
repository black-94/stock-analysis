package com.black.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class Finance163FundStockPage {
    String fundCode;
    String stockCode;
    BigDecimal stockNums;
    BigDecimal stockAmount;
    BigDecimal stockRatio;
    String date;

    public Finance163FundStockPage() {
    }
}
