package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StockPricePage {

    public StockPricePage() {
    }

    String code;
    String lastClose;
    String open;
    String cur;
    String high;
    String low;
    String volume;
    String amount;
    String percent;
    String change;
    String date;

}
