package com.black.repository;

import com.black.po.IpoStockPage;
import com.black.po.StockInfoPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockInfoRepository {

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

    @Insert("""
            insert into ipo_stock_page(code,name,market_day,market_year)
            values(#{code},#{name},#{marketDay},#{marketYear})
            """)
    void insert(StockInfoPO ipoStockPage);

    StockInfoPO recentBefore(String code, String date, int size);
}
