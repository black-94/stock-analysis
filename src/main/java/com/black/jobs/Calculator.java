package com.black.jobs;

import com.black.repository.Finance163Repository;
import com.black.repository.IpoStockPageRepository;
import com.black.repository.StockFinancePageRepository;
import com.black.repository.StockFundPageRepository;
import com.black.repository.StockInfoPageRepository;
import com.black.repository.StockInfoRepository;
import com.black.repository.StockNumPageRepository;
import com.black.repository.StockPriceHistoryPageRepository;
import com.black.repository.StockPricePageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Calculator {
    @Autowired
    Finance163Repository finance163Repository;
    @Autowired
    IpoStockPageRepository ipoStockPageRepository;
    @Autowired
    StockInfoPageRepository stockInfoPageRepository;
    @Autowired
    StockNumPageRepository stockNumPageRepository;
    @Autowired
    StockFinancePageRepository stockFinancePageRepository;
    @Autowired
    StockPricePageRepository stockPricePageRepository;
    @Autowired
    StockPriceHistoryPageRepository stockPriceHistoryPageRepository;
    @Autowired
    StockFundPageRepository stockFundPageRepository;
    @Autowired
    StockInfoRepository stockInfoRepository;

    public void dayJob(){









    }

    public void infoInit(String code){

    }

    public void quartlyInit(String code){

    }

    public void dayPriceInit(String code){

    }






}
