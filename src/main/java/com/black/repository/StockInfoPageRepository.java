package com.black.repository;

import com.black.po.StockInfoPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockInfoPageRepository {

    @Delete("truncate table stock_info_page")
    void deleteAll();

    @Delete("delete from stock_info_page where code=#{param1}")
    void deleteByCode(String code);

    @Insert("""
            insert into stock_info_page(code,name,biz,open_day,market_day) values
            (#{code},#{name},#{biz},#{openDay},#{marketDay})
            """)
    void insert(StockInfoPage stockInfoPage);

    @Select("select * from stock_info_page")
    List<StockInfoPage> queryAll();
}
