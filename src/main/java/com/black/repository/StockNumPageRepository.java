package com.black.repository;

import com.black.po.StockNumPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockNumPageRepository {

    @Delete("truncate table stock_num_page")
    void deleteAll();

    @Delete("delete from stock_num_page where code=#{param1} and `date`=#{param2}")
    void deleteByCode(String code, String date);

    @Insert("""
            insert into stock_num_page(code,name,biz,market_day,total,cycle,`date`) values
            (#{code},#{name},#{biz},#{marketDay},#{total},#{cycle},#{date})
            """)
    void insert(StockNumPage stockNumPage);

    @Select("select * from stock_num_page where code=#{param1} and `date` < ${param3} limit ${param2}")
    List<StockNumPage> queryRecentBefore(String code, int size, String date);

    @Select("select * from stock_num_page where code=#{param1} limit ${param2}")
    List<StockNumPage> queryRecent(String code, int size);

    @Select("select * from stock_num_page where `date`=#{param1}")
    List<StockNumPage> queryByDate(String date);
}
