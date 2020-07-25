package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class FundInfoPO {
    int id;
    String fundCode;
    String fundName;
    int historyInit;
    Date createTime;
    Date updateTime;

    public FundInfoPO() {
    }
}
