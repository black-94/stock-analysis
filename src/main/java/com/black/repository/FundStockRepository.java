package com.black.repository;

import com.black.po.FundStockPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface FundStockRepository {

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
    void batchInsert(List<FundStockPO> list);

    @Select("select * from fund_stock where fundCode=#{param2} and stockCode=#{param1} and `date`=#{param3}")
    FundStockPO queryByDate(String code, String fundCode, Date date);

    @Insert("insert into fund_stock(fundCode,stockCode,stockNums,stockAmount,stockRatio,`date`) " +
            "values(#{fundCode},#{stockCode},#{stockNums},#{stockAmount},#{stockRatio},#{date})")
    void insert(FundStockPO fundStockPO);
}
