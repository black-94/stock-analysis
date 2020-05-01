package com.black.repository;

import com.black.po.TaskPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TaskRepository extends MongoRepository<TaskPo,String> {

    @Query("{'scheduleTime':{ '$lt':?0 }, 'type':?1, 'status':0 }")
    Page<TaskPo> findRecentTasks(Long endTime,String type, Pageable pageable);

    @Query("{'scheduleCompleteTime':{ '$lt':?0 }, 'type':?1 , 'status':1 }")
    Page<TaskPo> findCompleteTasks(Long endTime,String type, Pageable pageable);

}
