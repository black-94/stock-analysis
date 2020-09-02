package com.black.repository;

import com.black.po.StockQuartlyReportPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface StockQuartlyReportRepository {
    List<StockQuartlyReportPO> queryBetween(String code, Date begin, Date end);

    StockQuartlyReportPO queryByCodeAndDate(String code, Date lastMonth);

    void insert(StockQuartlyReportPO reportPO);
}
