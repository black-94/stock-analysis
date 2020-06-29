package com.black.repository;


import com.black.po.StockFinancePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface StockHistoryFinanceRepository{

    @Select("select date from stock_finance where code = #{code}")
    List<Date> queryDateByCode(String code);

    @Insert("""
                insert into stock_finance(code,date,income,profit) values
                <foreach item="item" index="index" collection="param1" open="" separator="," close="">
                    (#{item.code},#{item.date},#{item.income},#{item.profit})
                </foreach>
            """)
    void batchInsert(List<StockFinancePo> stockFinancePos);
}
