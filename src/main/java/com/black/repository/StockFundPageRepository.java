package com.black.repository;

import com.black.po.StockFundPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockFundPageRepository {

    @Delete("truncate table stock_price_page")
    void deleteAll();

    @Delete("delete from stock_price_page where code=#{param1} and `date`=#{param2}")
    void deleteByCode(String code, String date);

    @Insert("""
            insert into stock_price_page(code,last_close,open,cur,high,low,volume,amount,`percent`,`change`,`date`)
            values(#{code},#{lastClose},#{open},#{cur},#{high},#{low},#{volume},#{amount},#{percent},#{change},#{date})
            """)
    void insert(StockFundPage stockPricePage);

    List<StockFundPage> queryByCodeAndDate(String code, String reportDay);

    void batchInsert(List<StockFundPage> insert);

    void deleteByCode(String code);
}
