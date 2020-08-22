package com.black.repository;

import com.black.po.IpoStockPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IpoStockPageRepository {

    @Delete("truncate table ipo_stock_page")
    void deleteAll();

    void batchInsert(List<IpoStockPage> ipoStockPages);

    @Select("select distinct(code) from ipo_stock_page")
    List<String> queryAllCodes();

    @Select("select * from ipo_stock_page where code=#{param1}")
    IpoStockPage queryByCode(String code);

    void insert(IpoStockPage ipoStockPage);

    List<String> queryByYear(String year);
}
