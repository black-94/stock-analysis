package com.black.po;

import lombok.Data;

@Data
public class SEPo {
    String code;
    String name;
    String country;

    public SEPo(){}

    public SEPo(String code, String name, String country) {
        this.code = code;
        this.name = name;
        this.country = country;
    }
}
