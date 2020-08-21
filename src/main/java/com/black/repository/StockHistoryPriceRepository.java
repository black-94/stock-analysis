package com.black.repository;


import com.black.pojo.StockHistoryPricePo;
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
                insert into stock_history_price(`code`,`lastClose`,`open`,`close`,`high`,`low`,`volume`,`amount`,`updown`,`percent`,`amplitude`,`total`,`capital`,`pe`,`date`) values
                <foreach item="item" index="index" collection="list" open="" separator="," close="">
                    (#{item.code},#{item.lastClose},#{item.open},#{item.close},#{item.high},#{item.low},#{item.volume},#{item.amount},#{item.updown},
                    #{item.percent},#{item.amplitude},#{item.total},#{item.capital},#{item.pe},#{item.date})
                </foreach>
                </script>
            """)
    void batchInsert(List<StockHistoryPricePo> list);
}
