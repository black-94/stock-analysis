package com.black.repository;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseRepository {
    public static final FastDateFormat formatter=FastDateFormat.getInstance("yyyy-MM-dd HH:mm:dd");

    @Autowired
    JdbcTemplate jdbcTemplate;

    protected <T> List<T> toList(List<Map<String,Object>> list,Class<T> clazz){
        return list.stream().map(e-> JSON.parseObject(JSON.toJSONString(e),clazz)).collect(Collectors.toList());
    }

    protected <T> T toObject(List<Map<String,Object>> list,Class<T> clazz){
        return toList(list,clazz).stream().findFirst().orElse(null);
    }

}
