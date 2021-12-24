package com.black.repository;

import com.black.po.IpoStockPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IpoStockPageRepository {

    @Delete("truncate table ipo_stock_page")
    void deleteAll();

    @Insert("                <script>\n" +
            "                insert into ipo_stock_page(code,name,market_day,market_year) values\n" +
            "                <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"\" separator=\",\" close=\"\">\n" +
            "                    (#{item.code},#{item.name},#{item.marketDay},#{item.marketYear})\n" +
            "                </foreach>\n" +
            "                </script>")
    void batchInsert(@Param("list") List<IpoStockPage> ipoStockPages);

    @Select("select distinct(code) from ipo_stock_page")
    List<String> queryAllCodes();

    @Select("select * from ipo_stock_page where code=#{param1}")
    IpoStockPage queryByCode(String code);

    @Insert("            insert into ipo_stock_page(code,name,market_day,market_year)\n" +
            "            values(#{code},#{name},#{marketDay},#{marketYear})")
    void insert(IpoStockPage ipoStockPage);

    @Select("select * from ipo_stock_page where market_year=#{param1}")
    List<String> queryByYear(String year);

    @Select("select * from ipo_stock_page")
    List<IpoStockPage> queryAll();

}
