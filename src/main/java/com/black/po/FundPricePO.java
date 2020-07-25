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
    String fundName;
    BigDecimal unit;
    BigDecimal ratio;
    BigDecimal amount;
    Date date;
    Date createTime;
    Date updateTime;

    public FundPricePO() {
    }
}
