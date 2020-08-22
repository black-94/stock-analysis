package com.black.repository;

import com.black.po.StockPriceHistoryPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockPriceHistoryPageRepository {

    @Delete("truncate table stock_price_history_page")
    void deleteAll();

    @Delete("delete from stock_price_history_page where code=#{param1}")
    void deleteByCode(String code);

    @Insert("""
                <script>
                insert into stock_history_price_page(`code`,`open`,`close`,`high`,`low`,`volume`,`amount`,`change`,`percent`,`amplitude`,`exchange`,`date`) values
                <foreach item="item" index="index" collection="list" open="" separator="," close="">
                    (#{item.code},#{item.open},#{item.close},#{item.high},#{item.low},#{item.volume},#{item.amount},#{item.change},
                    #{item.percent},#{item.amplitude},#{item.exchange},#{item.date})
                </foreach>
                </script>
            """)
    void batchInsert(@Param("list")List<StockPriceHistoryPage> stockPriceHistoryPages);

    @Select("select * from stock_price_history_page where code=#{param1} and `date`=#{param2}")
    StockPriceHistoryPage queryByCodeAndDate(String code, String date);

    @Insert("""
            insert into stock_history_price_page(`code`,`open`,`close`,`high`,`low`,`volume`,`amount`,`change`,`percent`,`amplitude`,`exchange`,`date`) values
            (#{code},#{open},#{close},#{high},#{low},#{volume},#{amount},#{change},#{percent},#{amplitude},#{exchange},#{date})
            """)
    void insert(StockPriceHistoryPage stockPriceHistoryPage);

}
