package com.black.repository;

import com.black.po.StockInfoPo;
import com.black.po.TaskPo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockInfoRepository extends MongoRepository<StockInfoPo,String> {
}
