package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class SEPo {
    @Id
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
