package com.black.repository;


import com.black.po.StockHistoryPricePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface StockHistoryPriceRepository{

    @Select("select date from stock_history_price where code = #{param1}")
    List<Date> queryDatesByCode(String code);

    @Insert("""
                insert into stock_history_price(code,open,lastClose,high,low) values
                <foreach item="item" index="index" collection="list" open="" separator="," close="">
                    (#{item.code},#{item.open},#{item.lastClose},#{item.high},#{item.low})
                </foreach>
            """)
    void batchInsert(List<StockHistoryPricePo> list);
}
