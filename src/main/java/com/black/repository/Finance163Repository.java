package com.black.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.black.po.Finance163StockHistoryFinancePO;
import com.black.po.Finance163StockHistoryPricePO;
import com.black.po.Finance163StockInfoPO;
import com.black.po.Finance163StockPricePO;
import com.black.util.NetUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Finance163Repository {

    public Finance163StockInfoPO queryInfo(String code, String exchanger){
        String url="http://quotes.money.163.com/f10/gszl_%s.html";
        String res = NetUtil.get(url, code);
        Document doc = Jsoup.parse(res);
        Elements elements = doc.select(".table_bg001 .td_label");

        String name="";
        String biz="";
        String openDay="";
        String marketDay="";
        for (Element e : elements) {
            String text = e.text();
            String value = e.nextElementSibling().text();
            switch (text){
                case "中文简称":name=value;break;
                case "主营业务":biz=value;break;
                case "上市日期":marketDay=value;break;
                case "成立日期":openDay=value;break;
                default:break;
            }
        }

        Finance163StockInfoPO po=new Finance163StockInfoPO();
        po.setCode(code);
        po.setName(name);
        po.setBiz(biz);
        po.setOpenDay(openDay);
        po.setMarketDay(marketDay);
        return po;
    }

    public Finance163StockPricePO queryCurPrice(String code, String exchanger){
        String key=(exchanger.contains("sz")?1:0)+code;
        String url="http://api.money.126.net/data/feed/%s,money.api?callback=a";
        String res = NetUtil.get(e->e.substring(2,e.length()-2),url,key);
        JSONObject json= JSON.parseObject(res).getJSONObject(key);
        String percent = json.getString("percent");
        String yestclose = json.getString("yestclose");
        String open = json.getString("open");
        String price = json.getString("price");
        String high = json.getString("high");
        String low = json.getString("low");
        String volume = json.getString("volume");
        String turnover = json.getString("turnover");
        String time = json.getString("time");
        String updown = json.getString("updown");
        String date = time.substring(0, 10);

        Finance163StockPricePO stockPricePo=new Finance163StockPricePO();
        stockPricePo.setCode(code);
        stockPricePo.setOpen(open);
        stockPricePo.setLastClose(yestclose);
        stockPricePo.setCur(price);
        stockPricePo.setHigh(high);
        stockPricePo.setLow(low);
        stockPricePo.setVolume(volume);
        stockPricePo.setAmount(turnover);
        stockPricePo.setUpdown(percent);
        stockPricePo.setChange(updown);
//        stockPricePo.setAmplitude();
        stockPricePo.setDate(date);

        return stockPricePo;
    }

    public List<Finance163StockHistoryPricePO> queryHistoryPrice(String code, String exchanger){
        String marketDay = queryInfo(code, exchanger).getMarketDay();
        int year=LocalDate.now().getYear();
        int marketYear=year-1;
        try {
            marketYear=Integer.valueOf(marketDay.substring(0,4));
        } catch (Exception e) {}

        List<Finance163StockHistoryPricePO> list=new ArrayList<>();
        for (int i = marketYear; i <= year; i++) {
            for (int j = 1; j < 5; j++) {
                String url="http://quotes.money.163.com/trade/lsjysj_%s.html?year=%d&season=%d";
                String res = NetUtil.get(url,code,i,j);
                Document doc = Jsoup.parse(res);
                Elements elements = doc.select(".table_bg001 tr");
                if(elements.size()<2){
                    continue;
                }
                for (int k = 1; k < elements.size(); k++) {
                    Element e = elements.get(k);
                    Elements tds = e.select("td");
                    String updown = tds.get(6).text();
                    String open = tds.get(1).text();
                    String close = tds.get(4).text();
                    String high = tds.get(2).text();
                    String low = tds.get(3).text();
                    String volume = tds.get(7).text();
                    String amount = tds.get(8).text();
                    String time = tds.get(0).text();
                    String change = tds.get(5).text();
                    String exchange = tds.get(10).text();
                    String amplitude = tds.get(9).text();

                    Finance163StockHistoryPricePO stockPricePo=new Finance163StockHistoryPricePO();
                    stockPricePo.setCode(code);
                    stockPricePo.setOpen(open);
                    stockPricePo.setHigh(high);
                    stockPricePo.setLow(low);
                    stockPricePo.setClose(close);
                    stockPricePo.setChange(change);
                    stockPricePo.setUpdown(updown);
                    stockPricePo.setVolume(volume);
                    stockPricePo.setAmount(amount);
                    stockPricePo.setAmplitude(amplitude);
                    stockPricePo.setExchange(exchange);
                    stockPricePo.setDate(time);

                    list.add(stockPricePo);
                }
            }
        }

        return list;
    }

    public List<Finance163StockHistoryFinancePO> queryHistoryFinance(String code, String exchanger){
        String url="http://quotes.money.163.com/f10/lrb_%s.html";
        String res = NetUtil.get(url,code);
        Document doc = Jsoup.parse(res);
        Elements elements = doc.select(".table_bg001.border_box.limit_sale.scr_table tr");
        Elements dates = elements.get(0).select("th");
        Elements incomes = elements.get(1).select("td");
        Elements profits = elements.get(40).select("td");

        if(dates.size()<1){
            return new ArrayList<>();
        }

        List<Finance163StockHistoryFinancePO> pos=new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            String time=dates.get(i).text();
            String income=incomes.get(i).text();
            String profit=profits.get(i).text();

            Finance163StockHistoryFinancePO po=new Finance163StockHistoryFinancePO();
            po.setCode(code);
            po.setDate(time);
            po.setIncome(income);
            po.setProfit(profit);

            pos.add(po);
        }

        return pos;
    }

}
