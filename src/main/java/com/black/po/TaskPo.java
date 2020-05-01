package com.black.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class TaskPo {
    @Id
    String id;
    String type;
    String params;
    Long scheduleTime;
    Long createTime;
    Long updateTime;
}
