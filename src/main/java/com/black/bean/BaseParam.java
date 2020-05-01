package com.black.bean;

import lombok.Data;
import java.util.Map;

@Data
public class BaseParam {
    String api_name;
    String token;
    Map<String,String> params;
    String fields;
}
