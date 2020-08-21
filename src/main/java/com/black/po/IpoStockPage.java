package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IpoStockPage {

    public IpoStockPage(){}

    String code;
    String marketDay;
    String marketYear;

}
