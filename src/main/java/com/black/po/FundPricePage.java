package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FundPricePage {
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

    public FundPricePage() {
    }
}