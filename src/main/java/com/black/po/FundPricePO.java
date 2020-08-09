package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class FundPricePO {
    long id;
    String fundCode;
    BigDecimal unit;
    BigDecimal ratio;
    BigDecimal amount;
    BigDecimal m1ret;
    BigDecimal m3ret;
    BigDecimal m6ret;
    BigDecimal m12ret;
    Date date;
    Date createTime;
    Date updateTime;

    public FundPricePO() {
    }
}
