package com.black.repository;

import com.black.po.StockInfoPo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StockInfoRepository {

    @Select("select distinct(code) from stock_info")
    List<String> queryAllCodes();

    @Insert("""
            insert into stock_info(code,exchanger,subExchanger) values
            <foreach item="item" index="index" collection="param1" open="" separator="," close="">
                (#{item.code},#{item.exchanger},#{item.subExchanger})
            </foreach>            
            """)
    @Options(useGeneratedKeys = true)
    void batchInsert(List<StockInfoPo> list);

    @Select("select * from stock_info where infoInit=0")
    List<StockInfoPo> queryUninitStock();

    @Update("""
            update stock_info set name=#{name}, biz=#{biz}, openDay=#{openDay}, marketDay=#{marketDay}, infoInit=1
            where id=#{id}
            """
    )
    void fillInfo(StockInfoPo stockInfoPo);

    @Select("select * from stock_info")
    List<StockInfoPo> queryAllStocks();

    @Update("update stock_info set ${param1}=#{param2} where id=#{param3}")
    void updateField(String name, String value, long id);
}
