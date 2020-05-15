package com.black.repository;

import com.black.po.StockExchangerPo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SERepository{

    public void insert(List<StockExchangerPo> list){
        String sql="insert into stock_exchange(code,name,country) values ";
        for (StockExchangerPo stockExchangerPo : list) {
            sql+=String.format("('%s','%s','%s')", stockExchangerPo.getCode(), stockExchangerPo.getName(), stockExchangerPo.getCountry())+",";
        }
        sql=sql.substring(0,sql.length()-1);
    }
}
