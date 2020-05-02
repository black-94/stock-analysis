package com.black.repository;

import com.black.po.TaskPo;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository extends BaseRepository{

    @Override
    protected Class getPoClass() {
        return TaskPo.class;
    }

    public TaskPo findRecentTasks(String type){
        Object o = sqlSessionTemplate.selectOne("select * from tb_task where scheduleTime < now() and status=0 and type='" + type + "' order by scheduleTime desc limit 1");
        return (TaskPo) toObject(o);
    }

    public TaskPo findCompleteTasks(String type) {
        Object o = sqlSessionTemplate.selectOne("select * from tb_task where status=1 and type='" + type + "' order by scheduleCompleteTime desc limit 1");
        return (TaskPo) toObject(o);
    }

    public void insert(TaskPo po){
        sqlSessionTemplate.insert("insert into tb_task(type,status,scheduleTime,scheduleCompleteTime) values (#{type},#{status},#{scheduleTime},#{scheduleCompleteTime})",po);
    }

}
