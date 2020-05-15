package com.black.jobs;

import com.black.po.*;
import com.black.repository.*;
import com.black.util.PoBuildUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Crawler {
    @Autowired
    EastMoneyRepository eastMoneyRepository;
    @Autowired
    Finance163Repository finance163Repository;
    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    StockPriceRepository stockPriceRepository;
    @Autowired
    StockHistoryFinanceRepository stockHistoryFinanceRepository;

    @Scheduled(cron = "0 0 16 * * ?")
    public void pullStockCodes(){
        List<String> codes = stockInfoRepository.queryAllCodes();
        List<StockInfoPo> stockInfoPos = eastMoneyRepository.queryAllStockCode();
        List<StockInfoPo> list = stockInfoPos.stream().filter(e -> !codes.contains(e)).collect(Collectors.toList());
        stockInfoRepository.batchInsert(list);
        initStockInfo();
    }

    public void initStockInfo(){
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryUninitStock();
        stockInfoPos.parallelStream().forEach(e->{
            Finance163StockInfoPO info = finance163Repository.queryInfo(e.getCode(), e.getExchanger());
            StockInfoPo stockInfoPo = PoBuildUtils.buildStockInfo(info);
            stockInfoRepository.fillInfo(stockInfoPo);
        });
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void pullStockPrice(){
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos.parallelStream().forEach(e->{
            Finance163StockPricePO price = finance163Repository.queryCurPrice(e.getCode(), e.getExchanger());
            StockPricePo stockPricePo = PoBuildUtils.buildStockPrice(price);
            stockPriceRepository.insert(stockPricePo);
        });
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void fillHistoryPrice(){
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos=stockInfoPos.stream().filter(e->e.getPriceComplete()==0).collect(Collectors.toList());
        stockInfoPos.parallelStream().forEach(e->{


        });
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void pullStockFinance(){

    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void fillHistoryFinance(){
        List<StockInfoPo> stockInfoPos = stockInfoRepository.queryAllStocks();
        stockInfoPos=stockInfoPos.stream().filter(e->e.getFinanceComplete()==0).collect(Collectors.toList());
        stockInfoPos.parallelStream().forEach(e->{
            List<Finance163StockHistoryFinancePO> finances = finance163Repository.queryHistoryFinance(e.getCode(), e.getExchanger());
            List<StockHistoryFinancePo> stockHistoryFinancePos = finances.parallelStream().map(PoBuildUtils::buildStockFinance).collect(Collectors.toList());
            List<Date> dates = stockHistoryFinanceRepository.queryDateByCode(e.getCode());
            stockHistoryFinancePos = stockHistoryFinancePos.stream().filter(shfp -> !dates.contains(shfp.getDate())).collect(Collectors.toList());
            stockHistoryFinanceRepository.batchInsert(stockHistoryFinancePos);
        });
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void fillFinanceCalRes(){

    }

}
