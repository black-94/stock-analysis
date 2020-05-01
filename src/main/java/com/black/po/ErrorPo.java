package com.black.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Builder
@Data
public class ErrorPo {
    @Id
    String id;
    String error;
    public ErrorPo(){}
}
