package com.black.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class StockInfoPo {
    @Id
    String code;
    String exchange;
    String name;
    String business;
    String openDay;
    Integer infoInit;
    Integer priceComplete;
    Integer financeComplete;
    Long createTime;
    Long updateTime;
}
