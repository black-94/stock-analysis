package com.black.repository;

import com.black.po.StockPricePo;
import com.black.po.TaskPo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockPriceRepository extends MongoRepository<StockPricePo,String> {
}
