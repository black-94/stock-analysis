package com.black.repository;


import com.black.po.StockPricePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Mapper
public interface StockPriceRepository{

    @Insert("""
            insert into stock_price(code,open,cur,lastClose,high,low,volume,amount,updown,change,amplitude,date)
            values (#{code},#{open},#{cur},#{lastClose},#{high},#{low},#{volume},#{amount},#{updown},#{change},#{amplitude},#{date})
            """)
    void insert(StockPricePo stockPricePo);
}
