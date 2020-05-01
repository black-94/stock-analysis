package com.black.repository;

import com.black.po.StockFinancePo;
import com.black.po.StockPricePo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockFinanceRepository extends MongoRepository<StockFinancePo,String> {
}
