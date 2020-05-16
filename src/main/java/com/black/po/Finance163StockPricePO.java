package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Finance163StockPricePO {

    public Finance163StockPricePO() {
    }

    String code;
    String exchanger;
    BigDecimal open;
    BigDecimal cur;
    BigDecimal lastClose;
    BigDecimal high;
    BigDecimal low;
    BigDecimal volumn;
    BigDecimal turnover;
    BigDecimal percent;
    Date date;

}
