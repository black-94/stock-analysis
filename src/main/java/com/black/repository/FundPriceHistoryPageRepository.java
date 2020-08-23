package com.black.repository;

import com.black.po.IpoStockPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FundPriceHistoryPageRepository {

    @Delete("truncate table ipo_stock_page")
    void deleteAll();

    @Insert("""
                <script>
                insert into ipo_stock_page(code,name,market_day,market_year) values
                <foreach item="item" index="index" collection="list" open="" separator="," close="">
                    (#{item.code},#{item.name},#{item.marketDay},#{item.marketYear})
                </foreach>
                </script>
            """)
    void batchInsert(@Param("list") List<IpoStockPage> ipoStockPages);

    @Select("select distinct(code) from ipo_stock_page")
    List<String> queryAllCodes();

    @Select("select * from ipo_stock_page where code=#{param1}")
    IpoStockPage queryByCode(String code);

    @Insert("""
            insert into ipo_stock_page(code,name,market_day,market_year)
            values(#{code},#{name},#{marketDay},#{marketYear})
            """)
    void insert(IpoStockPage ipoStockPage);

    @Select("select * from ipo_stock_page where market_year=#{param1}")
    List<String> queryByYear(String year);

    void queryByCodeAndDate(String fundCode, String date);
}
