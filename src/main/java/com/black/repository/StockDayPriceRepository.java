package com.black.repository;

import com.black.po.StockDayPricePO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StockDayPriceRepository {

    @Select("select * from stock_day_price where code=#{param1} and date=#{param2} limit ${param3}")
    StockDayPricePO recentBefore(String code, String date, int size);

    @Insert("insert into stock_day_price() values")
    void insert(StockDayPricePO stockDayPricePO);

}
