package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StockFundPage {

    public StockFundPage(){}

    String code;
    String fundCode;
    String stockNums;
    String stockAmount;
    String reportDay;

}
