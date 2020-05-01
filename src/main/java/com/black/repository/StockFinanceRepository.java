package com.black.repository;

import com.black.po.StockFinancePo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface StockFinanceRepository extends MongoRepository<StockFinancePo,String> {

    @Query("{'code':'?0','date':?1}")
    StockFinancePo findByCodeAndDate(String code,Long date);

}
