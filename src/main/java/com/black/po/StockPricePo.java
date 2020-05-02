package com.black.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class StockPricePo {
    @Id
    String id;
    String code;
    String name;
    String exchange;
    BigDecimal open;
    BigDecimal cur;
    BigDecimal lastClose;
    BigDecimal high;
    BigDecimal low;
    BigDecimal volumn;
    BigDecimal turnover;
    BigDecimal percent;
    Long date;
    Long createTime;
    Long updateTime;
}
