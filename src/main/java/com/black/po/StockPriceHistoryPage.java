package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StockPriceHistoryPage {

    public StockPriceHistoryPage() {
    }

    String code;
    String open;
    String high;
    String low;
    String close;
    String percent;
    String change;
    String amplitude;
    String volume;
    String amount;
    String exchange;
    String date;

}
