package com.black.repository;

import com.black.po.StockPricePo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StockPriceRepository extends MongoRepository<StockPricePo,String> {

    @Query("{'code':'?0','date':?1}")
    List<StockPricePo> findByCodeAndDate(String code,Long date);
}
