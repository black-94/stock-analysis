package com.black.repository;

import com.black.po.StockFundPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockFundPageRepository {

    @Delete("truncate table stock_price_page")
    void deleteAll();

    @Insert("""
            insert into stock_fund_page(code,fund_code,fund_name,stock_nums,stock_amount,report_day)
            values(#{code},#{lastClose},#{open},#{cur},#{high},#{low},#{volume},#{amount},#{percent},#{change},#{date})
            """)
    void insert(StockFundPage stockPricePage);

    @Select("")
    List<StockFundPage> queryByCodeAndDate(String code, String reportDay);

    @Insert("")
    void batchInsert(List<StockFundPage> insert);

    @Delete("")
    void deleteByCode(String code);
}
