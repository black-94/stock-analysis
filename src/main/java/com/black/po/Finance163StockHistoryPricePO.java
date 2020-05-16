package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Finance163StockHistoryPricePO {

    public Finance163StockHistoryPricePO() {
    }

    String code;
    String open;
    String high;
    String low;
    String close;
    String change;
    String updown;
    String volume;
    String amount;
    String amplitude;
    String exchange;
    String date;

}
