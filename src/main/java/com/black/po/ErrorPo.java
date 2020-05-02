package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
public class ErrorPo {
    @Id
    String id;
    String error;
    String createTime=new Date().toString();
    public ErrorPo(){}
    public ErrorPo(String msg){this.error=msg;}
}
