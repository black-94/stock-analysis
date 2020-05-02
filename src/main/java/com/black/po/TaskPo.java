package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@AllArgsConstructor
@Data
public class TaskPo {
    long id;
    String type;
    int status;
    Date scheduleTime;
    Date scheduleCompleteTime;
    Date createTime;
    Date updateTime;

    public TaskPo(){}
}
