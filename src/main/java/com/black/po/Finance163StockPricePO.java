package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Finance163StockPricePO {

    public Finance163StockPricePO() {
    }

    String code;
    String open;
    String lastClose;
    String cur;
    String high;
    String low;
    String volumn;
    String amount;
    String updown;
    String change;
    String amplitude;
    String date;

}
