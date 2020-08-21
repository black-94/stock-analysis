package com.black.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class FundStockPO {
    long id;
    String fundCode;
    String stockCode;
    BigDecimal stockNums;
    BigDecimal stockAmount;
    BigDecimal stockRatio;
    Date date;
    Date createTime;
    Date updateTime;

    public FundStockPO() {
    }
}
