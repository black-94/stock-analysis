package com.black.pojo;

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
    String marketDate;
    String type;
    String m1ret;
    String m3ret;
    String m6ret;
    String m12ret;

    public Finance163FundPricePO() {
    }
}
