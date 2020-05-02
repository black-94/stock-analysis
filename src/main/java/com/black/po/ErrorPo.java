package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
public class ErrorPo {
    long id;
    String type;
    String msg;
    String stack;
    Date createTime;
    public ErrorPo(){}
}
