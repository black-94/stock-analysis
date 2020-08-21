package com.black.repository;

import com.black.po.StockNumPage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockNumPageRepository {

    void deleteAll();


    void deleteByCode(String code, String date);

    void insert(StockNumPage stockNumPage);

}
