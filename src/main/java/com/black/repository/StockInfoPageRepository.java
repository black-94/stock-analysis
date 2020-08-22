package com.black.repository;

import com.black.po.StockInfoPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockInfoPageRepository {

    void deleteAll();


    void deleteByCode(String code);

    void insert(StockInfoPage stockInfoPage);

    List<StockInfoPage> queryAll();
}
