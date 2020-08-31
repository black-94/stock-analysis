package com.black.repository;

import com.black.po.StockFundPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface StockFundPageRepository {

    @Delete("truncate table stock_price_page")
    void deleteAll();

    @Insert("""
            insert into stock_fund_page(code,fund_code,fund_name,stock_nums,stock_amount,report_day)
            values(#{code},#{fundCode},#{fundName},#{stockNums},#{stockAmount},#{reportDay})
            """)
    void insert(StockFundPage stockPricePage);

    @Select("select * from stock_fund_page where code=#{param1} and report_day=#{param2}")
    List<StockFundPage> queryByCodeAndDate(String code, String reportDay);

    @Insert("""
                <script>
                insert into stock_fund_page(code,fund_code,fund_name,stock_nums,stock_amount,report_day) values
                <foreach item="item" index="index" collection="list" open="" separator="," close="">
                    (#{item.code},#{item.fundCode},#{item.fundName},#{item.stockNums},#{item.stockAmount},#{item.reportDay})
                </foreach>
                </script>
            """)
    void batchInsert(@Param("list") List<StockFundPage> insert);

    @Delete("delete from stock_price_page where code=#{param1}")
    void deleteByCode(String code);
}
