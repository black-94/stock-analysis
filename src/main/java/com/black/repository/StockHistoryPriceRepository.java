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
                <script>
                insert into stock_history_price(code,open,close,high,low,volume,turnover,percent,updown,amplitude,capital) values
                <foreach item="item" index="index" collection="list" open="" separator="," close="">
                    (#{item.code},#{item.open},#{item.close},#{item.high},#{item.low},#{item.volume},#{item.turnover},#{item.percent},
                    #{item.updown},#{item.amplitude},#{item.capital})
                </foreach>
                </script>
            """)
    void batchInsert(List<StockHistoryPricePo> list);
}
