package com.black.repository;


import com.black.po.StockFinancePo;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class StockFinanceRepository extends BaseRepository{

    public StockFinancePo findByCodeAndDate(String code, Date date){
        String str = formatter.format(date);
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from stock_finance where code=" + code + " and date='" + str + "'");
        return toObject(list, StockFinancePo.class);
    }

    public List<String> queryCodes(){
        return jdbcTemplate.queryForList("select distinct(code) from stock_finance",String.class);
    }

    public StockFinancePo findByCode(String code){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from stock_finance where code='" + code + "' order by createTime desc limit 1");
        return toObject(list,StockFinancePo.class);
    }

    public void save(StockFinancePo financePo) {
        if(financePo.getId()<=0){
            String sql="insert into stock_finance(code,name,exchange,date,income,y2yIncome,m2mIncome,profit,y2yProfit,m2mProfit) values(?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql,financePo.getCode(),financePo.getName(),financePo.getExchange(),financePo.getDate(),financePo.getIncome(),financePo.getY2yIncome(),financePo.getM2mIncome(),financePo.getProfit(),financePo.getY2yProfit(),financePo.getM2mProfit());
        }else{
            String sql="update stock_finance set name=?,exchange=?,income=?,y2yIncome=?,m2mIncome=?,profit=?,y2yProfit=?,m2mProfit=? where id=?";
            jdbcTemplate.update(sql,financePo.getName(),financePo.getExchange(),financePo.getIncome(),financePo.getY2yIncome(),financePo.getM2mIncome(),financePo.getProfit(),financePo.getY2yProfit(),financePo.getM2mProfit(),financePo.getId());
        }
    }
}
