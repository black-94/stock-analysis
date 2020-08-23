package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FundStockPage {
    String fundCode;
    String stockCode;
    String stockNums;
    String stockAmount;
    String stockRatio;
    String date;

    public FundStockPage() {
    }
}