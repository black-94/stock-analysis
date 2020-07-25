package com.black.repository;

import com.black.po.FundPricePO;
import com.black.po.StockInfoPo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface FundPriceRepository {

    @Select("select distinct(code) from stock_info")
    List<String> queryAllCodes();

    @Insert("""
            <script>
            insert into stock_info(code,exchanger,subExchanger,name,biz,openDay,marketDay) values
            <foreach item="item" index="index" collection="list" open="" separator="," close="">
                (#{item.code},#{item.exchanger},#{item.subExchanger},#{item.name},#{item.biz}
                ,#{item.openDay},#{item.marketDay})
            </foreach> 
            </script>           
            """)
    @Options(useGeneratedKeys = true)
    void batchInsert(List<StockInfoPo> list);

    @Select("select * from stock_info where infoInit=0")
    List<StockInfoPo> queryUninitStock();

    @Update("""
            update stock_info set name=#{name}, biz=#{biz}, openDay=#{openDay}, marketDay=#{marketDay}, infoInit=1
            where code=#{code}
            """
    )
    void fillInfo(StockInfoPo stockInfoPo);

    @Select("select * from stock_info")
    List<StockInfoPo> queryAllStocks();

    @Update("update stock_info set ${param1}=#{param2} where id=#{param3}")
    void updateField(String name, String value, long id);

    FundPricePO queryByDate(String fundCode, Date date);

    void insert(FundPricePO fundPricePO);
}
