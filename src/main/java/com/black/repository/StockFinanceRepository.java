package com.black.repository;


import com.black.po.StockFinancePo;

import java.util.Date;
import java.util.List;

public class StockFinanceRepository extends BaseRepository{

    @Override
    protected Class getPoClass() {
        return StockFinancePo.class;
    }

    public StockFinancePo findByCodeAndDate(String code, Date date){
        String str = formatter.format(date);
        Object o = sqlSessionTemplate.selectOne("select * from stock_finance where code=" + code + " and date='" + str + "'");
        return (StockFinancePo) toObject(o);
    }

    public List<String> queryCodes(){
        return sqlSessionTemplate.selectList("select distinct(code) from stock_finance");
    }

    public StockFinancePo findByCode(String code){
        Object o = sqlSessionTemplate.selectOne("select * from stock_finance where code='" + code + "' order by createTime desc limit 1");
        return (StockFinancePo) toObject(o);
    }

    public void save(StockFinancePo financePo) {
        sqlSessionTemplate.insert("insert into stock_finance(code,name,exchange,date,income,y2yIncome,m2mIncome,profit,y2yProfit,m2mProfit) values(#{code},#{name},#{exchange},#{date},#{income},#{y2yIncome},#{m2mIncome},#{profit},#{y2yProfit},#{m2mProfit})",financePo);
    }
}
