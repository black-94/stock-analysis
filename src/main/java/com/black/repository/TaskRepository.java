package com.black.repository;

import com.black.po.TaskPo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TaskRepository extends BaseRepository{

    public TaskPo findRecentTasks(String type){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from tb_task where scheduleTime < now() and status=0 and type='" + type + "' order by scheduleTime desc limit 1");
        return toObject(list,TaskPo.class);
    }

    public TaskPo findCompleteTasks(String type) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from tb_task where status=1 and type='" + type + "' order by scheduleCompleteTime desc limit 1");
        return toObject(list,TaskPo.class);
    }

    public void insert(TaskPo po){
        String sql="insert into tb_task(type,status,scheduleTime,scheduleCompleteTime) values (?,?,?,?)";
        jdbcTemplate.update(sql,po.getType(),po.getStatus(),po.getScheduleTime(),po.getScheduleCompleteTime());
    }

    public void update(TaskPo po){
        String sql="update tb_task set status=1 where id="+po.getId();
        jdbcTemplate.update(sql);
    }

}
