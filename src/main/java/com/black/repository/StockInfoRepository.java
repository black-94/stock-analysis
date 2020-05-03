package com.black.repository;


import com.black.po.StockInfoPo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class StockInfoRepository extends BaseRepository{

    public List<String> queryCodes(){
        return jdbcTemplate.queryForList("select distinct(code) from stock_info",String.class);
    }

    public List<StockInfoPo> queryByStatus(String field){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from stock_info where " + field + "=0");
        return toList(list,StockInfoPo.class);
    }

    public List<StockInfoPo> findAll() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from stock_info");
        return toList(list,StockInfoPo.class);
    }

    public void updateStatus(String field,long id){
        String sql="update stock_info set "+field+"=1 where id=?";
        jdbcTemplate.update(sql,id);
    }

    public void save(StockInfoPo po) {
        String sql="insert into stock_info(code,name,exchange,business,openDay,marketDay,infoInit,priceComplete,financeComplete) values (?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,po.getCode(),po.getName(),
                po.getExchange(),po.getBusiness(),
                po.getOpenDay(),po.getMarketDay(),
                po.getInfoInit(),po.getPriceComplete(),
                po.getFinanceComplete());
    }
}
