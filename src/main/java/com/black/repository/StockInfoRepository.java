package com.black.repository;


import com.black.po.StockInfoPo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StockInfoRepository extends BaseRepository{
    @Override
    protected Class getPoClass() {
        return StockInfoPo.class;
    }

    public List<String> queryCodes(){
        return sqlSessionTemplate.selectList("select distinct(code) from stock_info");
    }

    public List<StockInfoPo> queryByStatus(String field){
        List<Object> objects = sqlSessionTemplate.selectList("select * from stock_info where " + field + "=0");
        return objects.stream().map(this::toObject).map(e->(StockInfoPo)e).collect(Collectors.toList());
    }

    public List<StockInfoPo> findAll() {
        List<Object> objects = sqlSessionTemplate.selectList("select * from stock_info");
        return objects.stream().map(this::toObject).map(e->(StockInfoPo)e).collect(Collectors.toList());
    }

    public void save(StockInfoPo po) {
        sqlSessionTemplate.insert("insert into stock_info(code,name,exchange,business,openDay,marketDay,infoInit,priceComplete,financeComplete) values (#{code},#{name},#{exchange},#{business},#{openDay},#{marketDay},#{infoInit},#{priceComplete},#{financeComplete})",po);
    }
}
