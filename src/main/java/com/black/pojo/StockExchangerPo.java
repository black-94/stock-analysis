package com.black.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class StockExchangerPo {
    String code;
    String name;
    String country;

    public StockExchangerPo(){}

}
