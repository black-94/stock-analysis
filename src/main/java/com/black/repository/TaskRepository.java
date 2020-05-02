package com.black.repository;

import com.black.po.TaskPo;
import org.springframework.stereotype.Component;

@Component
public class TaskRepository extends BaseRepository{

    public TaskPo findRecentTasks(String type){
        return jdbcTemplate.queryForObject("select * from tb_task where scheduleTime < now() and status=0 and type='" + type + "' order by scheduleTime desc limit 1",TaskPo.class);
    }

    public TaskPo findCompleteTasks(String type) {
        return jdbcTemplate.queryForObject("select * from tb_task where status=1 and type='" + type + "' order by scheduleCompleteTime desc limit 1",TaskPo.class);
    }

    public void insert(TaskPo po){
        String sql="insert into tb_task(type,status,scheduleTime,scheduleCompleteTime) values (?,?,?,?)";
        jdbcTemplate.update(sql,po.getType(),po.getStatus(),po.getScheduleTime(),po.getScheduleCompleteTime());
    }

}
