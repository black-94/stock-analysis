package com.black.repository;

import com.black.po.FundStockPage;
import com.black.po.IpoStockPage;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface FundStockPageRepository {

    @Delete("truncate table ipo_stock_page")
    void deleteAll();

//    @Insert("""
//                <script>
//                insert into ipo_stock_page(code,name,market_day,market_year) values
//                <foreach item="item" index="index" collection="list" open="" separator="," close="">
//                    (#{item.code},#{item.name},#{item.marketDay},#{item.marketYear})
//                </foreach>
//                </script>
//            """)
//    void batchInsert(@Param("list") List<IpoStockPage> ipoStockPages);

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

    //    @Insert("""
//            <script>
//            insert into stock_info(code,exchanger,subExchanger,name,biz,openDay,marketDay) values
//            <foreach item="item" index="index" collection="list" open="" separator="," close="">
//                (#{item.code},#{item.exchanger},#{item.subExchanger},#{item.name},#{item.biz}
//                ,#{item.openDay},#{item.marketDay})
//            </foreach>
//            </script>
//            """)
    @Options(useGeneratedKeys = true)
    void batchInsert(List<FundStockPage> list);

    @Select("select * from fund_stock where fundCode=#{param2} and stockCode=#{param1} and `date`=#{param3}")
    FundStockPage queryByDate(String code, String fundCode, Date date);

    @Insert("insert into fund_stock(fundCode,stockCode,stockNums,stockAmount,stockRatio,`date`) " +
            "values(#{fundCode},#{stockCode},#{stockNums},#{stockAmount},#{stockRatio},#{date})")
    void insert(FundStockPage fundStockPO);
}
