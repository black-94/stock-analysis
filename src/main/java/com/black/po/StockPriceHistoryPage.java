package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockPriceHistoryPage {
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
