package com.black.repository;

import com.black.po.ErrorPo;
import org.springframework.stereotype.Component;

@Component
public class ErrorRepository extends BaseRepository{

    public void save(ErrorPo po){
        String sql="insert into tb_error_log(type,msg,stack)values(?,?,?)";
        jdbcTemplate.update(sql,po.getType(),po.getMsg(),po.getStack());
    }
}
