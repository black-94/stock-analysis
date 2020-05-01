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
    BigDecimal close;
    BigDecimal high;
    BigDecimal low;
    Long volumn;
    Long date;
    Long createTime;
    Long updateTime;
}
