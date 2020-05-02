package com.black.repository;


import com.black.po.StockInfoPo;

import java.util.List;
import java.util.stream.Collectors;

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
        return null;
    }

    public void save(StockInfoPo po) {
    }
}
