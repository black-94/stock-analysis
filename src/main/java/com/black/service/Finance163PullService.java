package com.black.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
            Elements elements = doc.select(".table_bg001 .dbrow .td_label");
            StockInfoPo po=new StockInfoPo();
            String name="";
            String business="";
            String openDay="";

            for (Element e : elements) {
                String text = e.text();
                String value = e.nextElementSibling().text();
                switch (text){
                    case "中文简称":name=value;break;
                    case "经营范围":business=value;break;
                    case "上市日期":openDay=value;break;
                    default:break;
                }
            }

            po.setCode(code);
            po.setExchange(market);
            po.setName(name);
            po.setBusiness(business);
            po.setOpenDay(openDay);
            po.setInfoInit(1);
            po.setPriceComplete(0);
            po.setFinanceComplete(0);
            po.setCreateTime(System.currentTimeMillis());
            po.setUpdateTime(System.currentTimeMillis());

            stockInfoRepository.save(po);
        } catch (Exception e) {
            errorRepository.save(new ErrorPo(Helper.stack(e)));
        }
    }

    public void pullPriceData() {
        List<StockInfoPo> all = stockInfoRepository.findAll();
        for (StockInfoPo stock : all) {
            try {
                String key=(stock.getName().contains("深")?1:0)+stock.getCode();
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
                long date = LocalDate.parse(time.substring(0, 10), DateTimeFormatter.ofPattern("yyyy/MM/dd")).atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond() * 1000L;

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
                stockPricePo.setDate(date);
                stockPricePo.setCreateTime(System.currentTimeMillis());
                stockPricePo.setUpdateTime(System.currentTimeMillis());

                if(stockPricePo.getCur().compareTo(stockPricePo.getLastClose())<0){
                    stockPricePo.getPercent().negate();
                }

                List<StockPricePo> po = stockPriceRepository.findByCodeAndDate(stockPricePo.getCode(), stockPricePo.getDate());
                if(!po.isEmpty()){
                    stockPricePo.setId(po.get(0).getId());
                }

                stockPriceRepository.save(stockPricePo);
            } catch (Exception e) {
                errorRepository.save(new ErrorPo(Helper.stack(e)));
            }
        }
    }

    public void pullHistoryPriceData(StockInfoPo stockInfoPo){
        int year=LocalDate.now().getYear();
        int openDay=Integer.valueOf(stockInfoPo.getOpenDay().substring(0,4));
        for (int i = openDay; i <= year; i++) {
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
                        long date = LocalDate.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond() * 1000L;

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
                        stockPricePo.setDate(date);
                        stockPricePo.setCreateTime(System.currentTimeMillis());
                        stockPricePo.setUpdateTime(System.currentTimeMillis());

                        List<StockPricePo> po = stockPriceRepository.findByCodeAndDate(stockPricePo.getCode(), stockPricePo.getDate());
                        if(!po.isEmpty()){
                            stockPricePo.setId(po.get(0).getId());
                        }

                        stockPriceRepository.save(stockPricePo);
                    } catch (Exception e) {
                        errorRepository.save(new ErrorPo(Helper.stack(e)));
                    }
                }
            }
        }
    }

    public void pullFinanceData(StockInfoPo stockInfoPo){
//        List<StockFinancePo> list=new ArrayList<>();
//        for (Object d : data) {
//            String t = decode(d.toString(), mapping);
//            JSONObject j= JSON.parseObject(t);
//            long date = LocalDateTime.parse(j.getString("reportdate")).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond() * 1000L;
//
//            StockFinancePo po=new StockFinancePo();
//            po.setCode(j.getString("scode"));
//            po.setName(j.getString("sname"));
//            po.setExchange(j.getString("trademarket"));
//            po.setIncome(decimalOf(j.getString("totaloperatereve")));
//            po.setY2yIncome(decimalOf(j.getString("ystz")));
//            po.setM2mIncome(decimalOf(j.getString("yshz")));
//            po.setProfit(decimalOf(j.getString("parentnetprofit")));
//            po.setY2yProfit(decimalOf(j.getString("sjltz")));
//            po.setM2mProfit(decimalOf(j.getString("sjlhz")));
//            po.setDate(date);
//            po.setCreateTime(System.currentTimeMillis());
//            po.setUpdateTime(System.currentTimeMillis());
//            list.add(po);
//        }
//        return list;
    }

    public String get(String str){
        try {
            URL url=new URL(str);
            InputStreamReader reader = new InputStreamReader(url.openConnection().getInputStream());
            StringWriter writer=new StringWriter();
            reader.transferTo(writer);
            return writer.toString();
        } catch (Exception e) {
            errorRepository.save(ErrorPo.builder().error("解析数据错误,url:"+str+",exception:"+ Helper.stack(e)).build());
            return null;
        }
    }

    public List<StockFinancePo> parse(JSONArray data, JSONArray mapping){
        List<StockFinancePo> list=new ArrayList<>();
        for (Object d : data) {
            String t = d.toString();
            JSONObject j= JSON.parseObject(t);
            long date = LocalDateTime.parse(j.getString("reportdate")).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond() * 1000L;

            StockFinancePo po=new StockFinancePo();
            po.setCode(j.getString("scode"));
            po.setName(j.getString("sname"));
            po.setExchange(j.getString("trademarket"));
            po.setIncome(decimalOf(j.getString("totaloperatereve")));
            po.setY2yIncome(decimalOf(j.getString("ystz")));
            po.setM2mIncome(decimalOf(j.getString("yshz")));
            po.setProfit(decimalOf(j.getString("parentnetprofit")));
            po.setY2yProfit(decimalOf(j.getString("sjltz")));
            po.setM2mProfit(decimalOf(j.getString("sjlhz")));
            po.setDate(date);
            po.setCreateTime(System.currentTimeMillis());
            po.setUpdateTime(System.currentTimeMillis());
            list.add(po);
        }
        return list;
    }

    public BigDecimal decimalOf(String str){
        try {
            return new BigDecimal(str);
        } catch (Exception e) {
            return null;
        }
    }

    public String reportDate(){
        int[] months={3,6,9,12};
        int monthValue = LocalDate.now().getMonthValue();
        int index = (monthValue - 1) / 3 -1;
        if(index==-1){
            index=3;
        }
        int month=months[index];
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), month + 1, 1).plus(-1, ChronoUnit.DAYS);
        return date.toString();
    }
}
