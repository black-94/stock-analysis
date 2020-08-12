package com.black.repository;


import com.black.po.StockPricePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface StockPriceRepository{

    @Insert("""
            insert into stock_price(`code`,`open`,`close`,`high`,`low`,`volume`,`amount`,`updown`,`percent`,`amplitude`,`total`,`num`,`capital`,`date`)
            values (#{code},#{open},#{close},#{high},#{low},#{volume},#{amount},#{updown},#{percent},#{amplitude},#{total},#{num},#{capital},#{date})
            """)
    void insert(StockPricePo stockPricePo);

    @Select("select * from stock_price where code=#{param1} and `date`=#{param2}")
    StockPricePo queryByDate(String code, Date date);
}
