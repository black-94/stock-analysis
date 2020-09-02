package com.black.repository;

import com.black.po.StockQuartlyReportPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface StockQuartlyReportRepository {
    @Select("select * from stock_quartly_report where code=#{param1} and report_day between #{param2} and #{param3}")
    List<StockQuartlyReportPO> queryBetween(String code, Date begin, Date end);

    @Select("")
    StockQuartlyReportPO queryByCodeAndDate(String code, Date lastMonth);

    @Insert("""
            insert into stock_quartly_report(`code`,`income`,`profit`,`total_income`,`total_profit`,`fund_ratio`,`m2m_income`,`m2m_profit`,`y2y_income`,`y2y_profit`,`report_day`)
            values(#{code},#{income},#{profit},#{totalIncome},#{totalProfit},#{fundRatio},#{m2mIncome},#{m2mProfit},#{y2yIncome},#{y2yProfit},#{reportDay})
            """)
    void insert(StockQuartlyReportPO reportPO);
}