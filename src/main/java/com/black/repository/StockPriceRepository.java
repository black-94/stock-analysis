package com.black.repository;


import com.black.po.StockPricePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface StockPriceRepository{

    @Insert("""
            insert into stock_price(`code`,`lastClose`,`open`,`close`,`high`,`low`,`volume`,`amount`,`updown`,`percent`,`amplitude`,`total`,`num`,`capital`,`date`)
            values (#{code},#{lastClose},#{open},#{close},#{high},#{low},#{volume},#{amount},#{updown},#{percent},#{amplitude},#{total},#{num},#{capital},#{date})
            """)
    void insert(StockPricePo stockPricePo);

    @Select("select * from stock_price where code=#{param1} and `date`=#{param2}")
    StockPricePo queryByDate(String code, Date date);

    @Select("select * from stock_price where code=#{param1} and `date` <= #{param2} order by `date` desc limit 1")
    StockPricePo queryOneBeforeDate(String code, Date date);
}
