package com.black.repository;


import com.black.po.StockPricePo;

import java.util.Date;
import java.util.List;

public class StockPriceRepository extends BaseRepository{
    @Override
    protected Class getPoClass() {
        return null;
    }

    public List<StockPricePo> findByCodeAndDate(String code, Date date){
        return null;
    }

    public void save(StockPricePo stockPricePo) {
    }


}
