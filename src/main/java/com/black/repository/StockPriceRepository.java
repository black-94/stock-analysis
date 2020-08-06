package com.black.repository;


import com.black.po.StockPricePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockPriceRepository{

    @Insert("""
            insert into stock_price(`code`,`open`,`close`,`high`,`low`,`volume`,`amount`,`updown`,`change`,`capital`,`date`)
            values (#{code},#{open},#{close},#{high},#{low},#{volume},#{amount},#{updown},#{change},#{capital},#{date})
            """)
    void insert(StockPricePo stockPricePo);
}
