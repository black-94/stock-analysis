package com.black.repository;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseRepository {
    public static final FastDateFormat formatter=FastDateFormat.getInstance("yyyy-MM-dd HH:mm:dd");

    @Autowired
    JdbcTemplate jdbcTemplate;

}
