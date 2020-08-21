package com.black.repository;

import com.black.po.StockPriceHistoryPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockPriceHistoryPageRepository {

    void deleteAll();


    void deleteByCode(String code);

    void batchInsert(List<StockPriceHistoryPage> stockPriceHistoryPages);

    StockPriceHistoryPage queryByCodeAndDate(String code, String date);

    void insert(StockPriceHistoryPage stockPriceHistoryPage);

}
