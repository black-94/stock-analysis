package com.black.repository;

import com.black.po.SEPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SERepository extends BaseRepository{

    public int insert(List<SEPo> list){
        String sql="insert into stock_exchange(code,name,country) values ";
        for (SEPo sePo : list) {
            sql+=String.format("(%s,%s,%s)",sePo.getCode(),sePo.getName(),sePo.getCountry())+",";
        }
        sql=sql.substring(0,sql.length()-1);
        return sqlSessionTemplate.insert(sql);
    }

    @Override
    protected Class getPoClass() {
        return SEPo.class;
    }
}
