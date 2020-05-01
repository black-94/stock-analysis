package com.black.repository;

import com.black.po.RequestPo;
import com.black.po.TaskPo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestRepository extends MongoRepository<RequestPo,String> {
}
