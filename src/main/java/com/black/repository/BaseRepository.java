package com.black.repository;

import org.apache.commons.lang3.time.FastDateFormat;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRepository {
    public static final FastDateFormat formatter=FastDateFormat.getInstance("yyyy-MM-dd HH:mm:dd");

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    protected abstract Class getPoClass();

    public Object toObject(Object o){
        if(o==null){
            return null;
        }
        try {
            Object t = getPoClass().getConstructor().newInstance();
            BeanUtils.copyProperties(o,t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
