package com.black.repository;

import com.black.po.ErrorPo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ErrorRepository extends MongoRepository<ErrorPo,String> {

}
