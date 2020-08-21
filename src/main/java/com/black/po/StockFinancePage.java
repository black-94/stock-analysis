package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StockFinancePage {

    public StockFinancePage() {
    }

    String code;
    String income;
    String profit;
    String reportDay;

}
