package com.black.repository;

import com.black.po.FundInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FundInfoRepository {

    @Select("select distinct(fundCode) from fund_info")
    List<String> queryAllCodes();

    @Select("select * from fund_info where historyInit=0")
    List<FundInfoPO> queryUninitFund();
}
