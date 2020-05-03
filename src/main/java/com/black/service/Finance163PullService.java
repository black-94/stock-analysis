package com.black.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.black.po.ErrorPo;
import com.black.po.StockFinancePo;
import com.black.po.StockInfoPo;
import com.black.po.StockPricePo;
import com.black.repository.ErrorRepository;
import com.black.repository.StockFinanceRepository;
import com.black.repository.StockInfoRepository;
import com.black.repository.StockPriceRepository;
import com.black.util.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class Finance163PullService {
    @Autowired
    ErrorRepository errorRepository;
    @Autowired
    StockFinanceRepository stockFinanceRepository;
    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    StockPriceRepository stockPriceRepository;

    public void pullStockInfo(String code,String market){
        String url=String.format("http://quotes.money.163.com/f10/gszl_%s.html",code);
        String res = get(url);
        if(res==null){
            return;
        }
        try {
            Document doc = Jsoup.parse(res);
            Elements elements = doc.select(".table_bg001 .td_label");
            StockInfoPo po=new StockInfoPo();
            String name="";
            String business="";
            String openDay="";
            String marketDay="";

            for (Element e : elements) {
                String text = e.text();
                String value = e.nextElementSibling().text();
                switch (text){
                    case "中文简称":name=value;break;
                    case "主营业务":business=value;break;
                    case "上市日期":marketDay=value;break;
                    case "成立日期":openDay=value;break;
                    default:break;
                }
            }

            po.setCode(code);
            po.setExchange(market);
            po.setName(name);
            po.setBusiness(business);
            po.setOpenDay(openDay);
            po.setMarketDay(marketDay);
            po.setInfoInit(1);
            po.setPriceComplete(0);
            po.setFinanceComplete(0);
            po.setCreateTime(new Date());
            po.setUpdateTime(new Date());

            stockInfoRepository.save(po);
        } catch (Exception e) {
            errorRepository.save(ErrorPo.builder().type(e.getClass().getName()).msg(e.getMessage()).stack(Helper.stack(e)).build());
        }
    }

    public void pullPriceData() {
        List<StockInfoPo> all = stockInfoRepository.findAll();
        for (StockInfoPo stock : all) {
            try {
                String key=(stock.getExchange().contains("深")?1:0)+stock.getCode();
                String url=String.format("http://api.money.126.net/data/feed/%s,money.api?callback=a",key);
                String res = get(url);
                if(res==null){
                    continue;
                }
                res=res.substring(2,res.length()-2);
                JSONObject json=JSON.parseObject(res).getJSONObject(key);
                String percent = json.getString("percent");
                String yestclose = json.getString("yestclose");
                String open = json.getString("open");
                String price = json.getString("price");
                String high = json.getString("high");
                String low = json.getString("low");
                String volumn = json.getString("volumn");
                String turnover = json.getString("turnover");
                String time = json.getString("time");
                Instant date = LocalDate.parse(time.substring(0, 10), DateTimeFormatter.ofPattern("yyyy/MM/dd")).atStartOfDay(ZoneId.systemDefault()).toInstant();

                StockPricePo stockPricePo=new StockPricePo();
                stockPricePo.setCode(stock.getCode());
                stockPricePo.setName(stock.getName());
                stockPricePo.setExchange(stock.getExchange());
                stockPricePo.setPercent(decimalOf(percent));
                stockPricePo.setOpen(decimalOf(open));
                stockPricePo.setCur(decimalOf(price));
                stockPricePo.setLastClose(decimalOf(yestclose));
                stockPricePo.setHigh(decimalOf(high));
                stockPricePo.setLow(decimalOf(low));
                stockPricePo.setVolumn(decimalOf(volumn));
                stockPricePo.setTurnover(decimalOf(turnover));
                stockPricePo.setDate(Date.from(date));
                stockPricePo.setCreateTime(new Date());
                stockPricePo.setUpdateTime(new Date());

                if(stockPricePo.getCur().compareTo(stockPricePo.getLastClose())<0){
                    stockPricePo.getPercent().negate();
                }

                List<StockPricePo> po = stockPriceRepository.findByCodeAndDate(stockPricePo.getCode(), stockPricePo.getDate());
                if(!po.isEmpty()){
                    stockPricePo.setId(po.get(0).getId());
                }

                stockPriceRepository.save(stockPricePo);
            } catch (Exception e) {
                 errorRepository.save(ErrorPo.builder().type(e.getClass().getName()).msg(e.getMessage()).stack(Helper.stack(e)).build());
            }
        }
    }

    public void pullHistoryPriceData(StockInfoPo stockInfoPo){
        int year=LocalDate.now().getYear();
        int marketYear=year-1;
        try {
            marketYear=Integer.valueOf(stockInfoPo.getMarketDay().substring(0,4));
        } catch (Exception e) {}
        for (int i = marketYear; i <= year; i++) {
            for (int j = 1; j < 5; j++) {
                String url=String.format("http://quotes.money.163.com/trade/lsjysj_%s.html?year=%d&season=%d",stockInfoPo.getCode(),i,j);
                String res = get(url);
                Document doc = Jsoup.parse(res);
                Elements elements = doc.select(".table_bg001 tr");
                if(elements.size()<2){
                    continue;
                }
                for (int k = 1; k < elements.size(); k++) {
                    try {
                        Element e = elements.get(k);
                        Elements tds = e.select("td");
                        String percent = tds.get(6).text();
                        String open = tds.get(1).text();
                        String close = tds.get(4).text();
                        String high = tds.get(2).text();
                        String low = tds.get(3).text();
                        String volumn = tds.get(7).text();
                        String turnover = tds.get(8).text();
                        String time = tds.get(0).text();
                        Instant date = LocalDate.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant();

                        StockPricePo stockPricePo=new StockPricePo();
                        stockPricePo.setCode(stockInfoPo.getCode());
                        stockPricePo.setName(stockInfoPo.getName());
                        stockPricePo.setExchange(stockInfoPo.getExchange());
                        stockPricePo.setPercent(decimalOf(percent));
                        stockPricePo.setOpen(decimalOf(open));
                        stockPricePo.setCur(decimalOf(close));
                        stockPricePo.setHigh(decimalOf(high));
                        stockPricePo.setLow(decimalOf(low));
                        stockPricePo.setVolumn(decimalOf(volumn));
                        stockPricePo.setTurnover(decimalOf(turnover));
                        stockPricePo.setDate(Date.from(date));
                        stockPricePo.setCreateTime(new Date());
                        stockPricePo.setUpdateTime(new Date());

                        List<StockPricePo> po = stockPriceRepository.findByCodeAndDate(stockPricePo.getCode(), stockPricePo.getDate());
                        if(!po.isEmpty()){
                            stockPricePo.setId(po.get(0).getId());
                        }

                        stockPriceRepository.save(stockPricePo);
                    } catch (Exception e) {
                         errorRepository.save(ErrorPo.builder().type(e.getClass().getName()).msg(e.getMessage()).stack(Helper.stack(e)).build());
                    }
                }
            }
        }

        stockInfoPo.setPriceComplete(1);
        stockInfoPo.setUpdateTime(new Date());
        stockInfoRepository.save(stockInfoPo);
    }

    public void pullFinanceData(StockInfoPo stockInfoPo){
        String url=String.format("http://quotes.money.163.com/f10/zycwzb_%s,season.html",stockInfoPo.getCode());
        String res = get(url);
        Document doc = Jsoup.parse(res);
        Elements elements = doc.select(".table_bg001.border_box.limit_sale.scr_table tr");
        Elements dates = elements.get(0).select("th");
        Elements incomes = elements.get(4).select("td");
        Elements profits = elements.get(10).select("td");

        if(dates.size()<1){
            return;
        }

        List<StockFinancePo> pos=new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            StockFinancePo po=new StockFinancePo();
            try {
                BeanUtils.copyProperties(stockInfoPo,po);
                String time=dates.get(i).text();
                String income=incomes.get(i).text();
                String profit=profits.get(i).text();
                Instant date = LocalDate.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant();
                po.setId(0);
                po.setDate(Date.from(date));
                po.setIncome(decimalOf(income));
                po.setProfit(decimalOf(profit));
                po.setCreateTime(null);
                po.setUpdateTime(new Date());
            } catch (Exception e) {
                errorRepository.save(ErrorPo.builder().type(e.getClass().getName()).msg(e.getMessage()).stack(Helper.stack(e)).build());
            }
            pos.add(po);
        }

        for (int i = 0; i < pos.size(); i++) {
            try {
                BigDecimal income=pos.get(i).getIncome();
                BigDecimal profit=pos.get(i).getProfit();
                BigDecimal yincome=null;
                BigDecimal yprofit=null;
                BigDecimal mincome=null;
                BigDecimal mprofit=null;
                if(i+1<pos.size()){
                    mincome=pos.get(i+1).getIncome();
                    mprofit=pos.get(i+1).getProfit();
                }
                if(i+4<pos.size()){
                    yincome=pos.get(i+4).getIncome();
                    yprofit=pos.get(i+4).getProfit();
                }

                StockFinancePo po = pos.get(i);
                po.setY2yIncome(calRatio(yincome,income));
                po.setY2yProfit(calRatio(yprofit,profit));
                po.setM2mIncome(calRatio(mincome,income));
                po.setM2mProfit(calRatio(mprofit,profit));
            } catch (Exception e) {
                 errorRepository.save(ErrorPo.builder().type(e.getClass().getName()).msg(e.getMessage()).stack(Helper.stack(e)).build());
            }
        }

        for (StockFinancePo po : pos) {
            StockFinancePo tmp = stockFinanceRepository.findByCodeAndDate(po.getCode(), po.getDate());
            if(tmp==null){
                stockFinanceRepository.save(po);
            }
        }

        stockInfoPo.setFinanceComplete(1);
        stockInfoPo.setUpdateTime(new Date());
        stockInfoRepository.save(stockInfoPo);
    }

    public String get(String str){
        for (int i = 0; i < 3; i++) {
            try {
                URL url=new URL(str);
                InputStreamReader reader = new InputStreamReader(url.openConnection().getInputStream());
                StringWriter writer=new StringWriter();
                reader.transferTo(writer);
                return writer.toString();
            } catch (Exception e) {
                errorRepository.save(ErrorPo.builder().type(e.getClass().getName()).msg(e.getMessage()).stack(Helper.stack(e)).build());
            }
        }
        return null;
    }

    public BigDecimal calRatio(BigDecimal base,BigDecimal change){
        if(base==null||change==null){
            return null;
        }

        if(base.compareTo(BigDecimal.ZERO)==0){
            return null;
        }

        return change.subtract(base).divide(base).setScale(5, RoundingMode.HALF_DOWN);
    }

    public BigDecimal decimalOf(String str){
        try {
            if(str!=null){
                str=str.replace(",","").replace(" ","").replace("-","");
            }
            return new BigDecimal(str);
        } catch (Exception e) {
            return null;
        }
    }
}
