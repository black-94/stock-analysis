package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagePO {
    private Long id;
    private String type;
    private String msg;
    private Date createTime;
}
