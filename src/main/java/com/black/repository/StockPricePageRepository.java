package com.black.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockPricePageRepository {

    void deleteAll();


    void deleteByCode(String code, String date);
}
