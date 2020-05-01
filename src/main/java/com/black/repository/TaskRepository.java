package com.black.repository;

import com.black.po.TaskPo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskPo,String> {
}
