package com.black.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Finance163FundPriceHistoryPage {
    String fundCode;
    String unit;
    String historyUnit;
    String ratio;
    String amount;
    String date;

    public Finance163FundPriceHistoryPage() {
    }
}
