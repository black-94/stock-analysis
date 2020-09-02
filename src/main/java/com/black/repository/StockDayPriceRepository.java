package com.black.repository;

import com.black.po.StockDayPricePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockDayPriceRepository {


    StockDayPricePO recentBefore(String code, String date, int size);

    void insert(StockDayPricePO stockDayPricePO);

}
