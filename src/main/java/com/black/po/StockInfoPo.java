package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Builder
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

    public StockInfoPo(){}
}
