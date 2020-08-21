package com.black.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Finance163StockHistoryFinancePO {

    public Finance163StockHistoryFinancePO() {
    }

    String code;
    String income;
    String profit;
    String date;

}
