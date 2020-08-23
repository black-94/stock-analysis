package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FundPriceHistoryPage {
    String fundCode;
    String unit;
    String historyUnit;
    String ratio;
    String amount;
    String date;

    public FundPriceHistoryPage() {
    }
}