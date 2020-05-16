package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
public class StockInfoPo {
    long id;
    String code;
    String name;
    String exchanger;
    String biz;
    String openDay;
    String marketDay;
    int infoInit;
    int priceComplete;
    int financeComplete;
    Date createTime;
    Date updateTime;

    public StockInfoPo(){}
}
