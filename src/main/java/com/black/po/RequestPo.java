package com.black.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class RequestPo {
    @Id
    String id;
    String sourceType;
    String url;
    String params;
    String body;
    String date;
    Long createTime;
    Long updateTime;
}
