package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StockInfoPage {

    public StockInfoPage(){}

    String code;
    String name;
    String biz;
    String openDay;
    String marketDay;

}
