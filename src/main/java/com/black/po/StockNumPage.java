package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StockNumPage {

    public StockNumPage() {
    }

    String code;
    String name;
    String biz;
    String marketDay;
    String total;
    String cycle;
    String date;

}
