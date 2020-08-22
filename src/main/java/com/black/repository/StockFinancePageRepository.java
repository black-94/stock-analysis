package com.black.repository;

import com.black.po.StockFinancePage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockFinancePageRepository {

    @Delete("truncate table stock_finance_page")
    void deleteAll();

    @Insert("""
                <script>
                insert into stock_finance_page(`code`,`report_day`,`income`,`profit`) values
                <foreach item="item" index="index" collection="list" open="" separator="," close="">
                    (#{item.code},#{item.reportDay},#{item.income},#{item.profit})
                </foreach>
                </script>
            """)
    void batchInsert(@Param("list")List<StockFinancePage> stockFinancePages);

    @Select("select * from stock_finance_page where code=#{param1}")
    List<StockFinancePage> queryByCode(String code);
}
