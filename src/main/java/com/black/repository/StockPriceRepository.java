package com.black.repository;


import com.black.po.StockInfoPo;
import com.black.po.StockPricePo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class StockPriceRepository extends BaseRepository{

    public List<StockPricePo> findByCodeAndDate(String code, Date date){
        String sql="select * from stock_price where code=? and date=?";
        return jdbcTemplate.queryForList(sql,new Object[]{code,date}, StockPricePo.class);
    }

    public void save(StockPricePo stockPricePo) {
        String sql="insert into stock_price(code,name,exchange,open,cur,lastClose,high,low,volumn,turnover,percent,date) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,stockPricePo.getCode(),stockPricePo.getName()
                ,stockPricePo.getExchange(),stockPricePo.getOpen()
                ,stockPricePo.getCur(),stockPricePo.getLastClose()
                ,stockPricePo.getHigh(),stockPricePo.getLow()
                ,stockPricePo.getVolumn(),stockPricePo.getTurnover()
                ,stockPricePo.getPercent(),stockPricePo.getDate());
    }

}
