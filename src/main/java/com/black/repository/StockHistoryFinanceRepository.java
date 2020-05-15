package com.black.repository;


import com.black.po.StockHistoryFinancePo;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class StockHistoryFinanceRepository{

    public StockHistoryFinancePo findByCodeAndDate(String code, Date date){
        return null;
//        String str = formatter.format(date);
//        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from stock_finance where code=" + code + " and date='" + str + "'");
    }

    public List<String> queryCodes(){
        return null;
//        return jdbcTemplate.queryForList("select distinct(code) from stock_finance",String.class);
    }

    public StockHistoryFinancePo findByCode(String code){
        return null;
//        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from stock_finance where code='" + code + "' order by createTime desc limit 1");
//        return toObject(list, StockHistoryFinancePo.class);
    }

    public void save(StockHistoryFinancePo financePo) {

//        if(financePo.getId()<=0){
//            String sql="insert into stock_finance(code,name,exchange,date,income,y2yIncome,m2mIncome,profit,y2yProfit,m2mProfit) values(?,?,?,?,?,?,?,?,?,?)";
//            jdbcTemplate.update(sql,financePo.getCode(),financePo.getName(),financePo.getExchange(),financePo.getDate(),financePo.getIncome(),financePo.getY2yIncome(),financePo.getM2mIncome(),financePo.getProfit(),financePo.getY2yProfit(),financePo.getM2mProfit());
//        }else{
//            String sql="update stock_finance set name=?,exchange=?,income=?,y2yIncome=?,m2mIncome=?,profit=?,y2yProfit=?,m2mProfit=? where id=?";
//            jdbcTemplate.update(sql,financePo.getName(),financePo.getExchange(),financePo.getIncome(),financePo.getY2yIncome(),financePo.getM2mIncome(),financePo.getProfit(),financePo.getY2yProfit(),financePo.getM2mProfit(),financePo.getId());
//        }
    }

    public List<Date> queryDateByCode(String code) {
        return null;
    }

    public void batchInsert(List<StockHistoryFinancePo> stockHistoryFinancePos) {
        
    }
}
