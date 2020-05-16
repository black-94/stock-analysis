package com.black.repository;


import com.black.po.StockInfoPo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StockInfoRepository{

    @Select("select distinct(code) from stock_info")
    public List<String> queryAllCodes();

    @Insert("""
            insert into stock_info(code,name,exchanger,biz,openDay,marketDay) values
            <foreach item="item" index="index" collection="param1" open="" separator="," close="">
                (#{item.code},#{item.name},#{item.exchanger},#{item.biz},#{item.openDay},#{item.marketDay})
            </foreach>            
            """)
    @Options(useGeneratedKeys = true)
    public void batchInsert(List<StockInfoPo> list);

    @Select("select * from stock_info where infoInit=0")
    public List<StockInfoPo> queryUninitStock();

    @Update("update stock_info set biz=#{biz},openDay=#{openDay},marketDay=#{marketDay} where id=#{id}")
    public void fillInfo(StockInfoPo stockInfoPo);

    @Select("select * from stock_info")
    public List<StockInfoPo> queryAllStocks();
}
