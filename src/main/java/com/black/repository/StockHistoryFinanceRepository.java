package com.black.repository;


import com.black.pojo.StockFinancePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface StockHistoryFinanceRepository{

    @Select("select `date` from stock_finance where code = #{param1}")
    List<Date> queryDateByCode(String code);

    @Insert("""
                <script>
                insert into stock_finance(`code`,`date`,`income`,`profit`) values
                <foreach item="item" index="index" collection="list" open="" separator="," close="">
                    (#{item.code},#{item.date},#{item.income},#{item.profit})
                </foreach>
                </script>
            """)
    void batchInsert(@Param("list") List<StockFinancePo> stockFinancePos);

    @Select("select * from stock_finance where code=#{param1}")
    List<StockFinancePo> queryByCode(String code);

    @Update("update stock_finance set ${param1}=#{param2} where id=#{param3}")
    void updateField(String name, String value, long id);
}
