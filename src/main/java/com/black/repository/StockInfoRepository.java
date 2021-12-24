package com.black.repository;

import com.black.po.StockInfoPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockInfoRepository {

    @Select("select distinct(code) from stock_info")
    List<String> queryAllCodes();

    @Insert("            insert into stock_info(`code`,`name`,`biz`,`open_day`,`market_day`)\n" +
            "            values(#{code},#{name},#{biz},#{openDay},#{marketDay})")
    void insert(StockInfoPO ipoStockPage);

    @Select("select * from stock_info")
    List<StockInfoPO> queryAll();
}
