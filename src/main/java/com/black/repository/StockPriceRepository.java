package com.black.repository;


import com.black.po.StockPricePo;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class StockPriceRepository extends BaseRepository{

    public List<StockPricePo> findByCodeAndDate(String code, Date date){
        String sql="select * from stock_price where code=? and date=?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[]{code, date});
        return toList(list, StockPricePo.class);
    }

    public void save(StockPricePo stockPricePo) {
        if(stockPricePo.getId()<=0){
            String sql="insert into stock_price(code,name,exchange,open,cur,lastClose,high,low,volumn,turnover,percent,date) values (?,?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql,stockPricePo.getCode(),stockPricePo.getName()
                    ,stockPricePo.getExchange(),stockPricePo.getOpen()
                    ,stockPricePo.getCur(),stockPricePo.getLastClose()
                    ,stockPricePo.getHigh(),stockPricePo.getLow()
                    ,stockPricePo.getVolumn(),stockPricePo.getTurnover()
                    ,stockPricePo.getPercent(),stockPricePo.getDate());
        }else{
            String sql="update stock_price set name=?,exchange=?,open=?,cur=?,lastClose=?,high=?,low=?,volumn=?,turnover=?,percent=? where id=?";
            jdbcTemplate.update(sql,stockPricePo.getName()
                    ,stockPricePo.getExchange(),stockPricePo.getOpen()
                    ,stockPricePo.getCur(),stockPricePo.getLastClose()
                    ,stockPricePo.getHigh(),stockPricePo.getLow()
                    ,stockPricePo.getVolumn(),stockPricePo.getTurnover()
                    ,stockPricePo.getPercent(),stockPricePo.getId());
        }
    }

}
