package com.black.po;

import lombok.Data;

import java.util.Date;

@Data
public class StockInfoPO {
    String code;
    String name;
    String biz;
    Date openDay;
    Date marketDay;
}
