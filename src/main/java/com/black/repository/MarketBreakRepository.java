package com.black.repository;

import com.black.po.MarketBreakPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface MarketBreakRepository {

    @Select("select * from market_break where timezone=#{param1} and break_date < #{param3} and break_date > #{param2}")
    List<MarketBreakPO> queryByTimezone(String timeZone, Date begin, Date end);

}
