package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Finance163FundStockPO {
    String fundCode;
    String stockCode;
    String stockNums;
    String stockAmount;
    String stockRatio;

    public Finance163FundStockPO() {
    }
}
