package com.black.repository;

import com.black.pojo.FundPricePO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface FundPriceRepository {

    @Select("select * from fund_code where `date`=#{date} and fundCode=#{fundCode}")
    FundPricePO queryByDate(@Param("fundCode") String fundCode, @Param("date") Date date);

    @Insert("insert into fund_info(fundCode,unit,amount,ratio) values(#{fundCode},#{unit},#{amount},#{ratio})")
    void insert(FundPricePO fundPricePO);
}
