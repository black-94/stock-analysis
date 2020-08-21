package com.black.repository;

import com.black.po.StockPricePage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockPricePageRepository {

    void deleteAll();


    void deleteByCode(String code, String date);

    void insert(StockPricePage stockPricePage);
}
