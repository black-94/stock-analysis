package com.black.repository;

import com.black.po.MessagePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface MessageRepository {

    String FINANCE_REPORT_MESSAGE = "finance_report";

    @Select("select * from message where `type`=#{param1} and `date`=#{param2}")
    List<MessagePO> queryByTypeAndDate(String type, Date date);

}
