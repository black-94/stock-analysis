package com.black.repository;

import com.black.po.IpoStockPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IpoStockPageRepository {

    void deleteAll();


    void batchInsert(List<IpoStockPage> ipoStockPages);

    List<String> queryAllCodes();
}
