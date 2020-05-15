package com.black.po;

import lombok.Data;

@Data
public class StockExchangerPo {
    String code;
    String name;
    String country;

    public StockExchangerPo(){}

    public StockExchangerPo(String code, String name, String country) {
        this.code = code;
        this.name = name;
        this.country = country;
    }
}
