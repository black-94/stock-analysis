package com.black.repository;

import com.black.po.SEPo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SERepository extends MongoRepository<SEPo,String> {

}
