package com.black.repository;

import com.black.po.ErrorPo;
import org.springframework.stereotype.Repository;

@Repository
public class ErrorRepository extends BaseRepository{

    public void save(ErrorPo po){
        sqlSessionTemplate.insert("insert into tb_error_log(type,msg,stack)values(#{type},#{msg},#{stack})",po);
    }

    @Override
    protected Class getPoClass() {
        return ErrorPo.class;
    }
}
