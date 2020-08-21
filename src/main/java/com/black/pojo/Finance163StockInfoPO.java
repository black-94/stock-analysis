package com.black.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Finance163StockInfoPO {

    public Finance163StockInfoPO(){}

    String code;
    String name;
    String biz;
    String openDay;
    String marketDay;
}