package com.black.repository;

import com.black.po.StockFinancePage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockFinancePageRepository {

    void deleteAll();


    void deleteByCode(String code);

    void batchInsert(List<StockFinancePage> stockFinancePages);

    List<StockFinancePage> queryByCode(String code);
}
