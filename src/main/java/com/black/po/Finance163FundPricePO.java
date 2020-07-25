package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Finance163FundPricePO {
    String fundCode;
    String fundName;
    String unit;
    String ratio;
    String amount;
    String date;

    public Finance163FundPricePO() {
    }
}
