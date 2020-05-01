package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Builder
@AllArgsConstructor
@Data
public class TaskPo {
    @Id
    String id;
    String type;
    String params;
    Integer status;
    Long scheduleTime;
    Long scheduleCompleteTime;
    Long createTime;
    Long updateTime;

    public TaskPo(){}
}
