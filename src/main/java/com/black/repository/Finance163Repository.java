package com.black.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.black.po.*;
import com.black.util.NetUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static com.black.util.Helper.decimalOf;

@Repository
public class Finance163Repository {

    @Autowired
    StockPriceRepository stockPriceRepository;

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
        po.setExchanger(exchanger);
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
        String volumn = json.getString("volumn");
        String turnover = json.getString("turnover");
        String time = json.getString("time");
        Instant date = LocalDate.parse(time.substring(0, 10), DateTimeFormatter.ofPattern("yyyy/MM/dd")).atStartOfDay(ZoneId.systemDefault()).toInstant();

        Finance163StockPricePO stockPricePo=new Finance163StockPricePO();
        stockPricePo.setCode(code);
        stockPricePo.setExchanger(exchanger);
        stockPricePo.setPercent(decimalOf(percent));
        stockPricePo.setOpen(decimalOf(open));
        stockPricePo.setCur(decimalOf(price));
        stockPricePo.setLastClose(decimalOf(yestclose));
        stockPricePo.setHigh(decimalOf(high));
        stockPricePo.setLow(decimalOf(low));
        stockPricePo.setVolumn(decimalOf(volumn));
        stockPricePo.setTurnover(decimalOf(turnover));
        stockPricePo.setDate(Date.from(date));

        if(stockPricePo.getCur().compareTo(stockPricePo.getLastClose())<0){
            stockPricePo.getPercent().negate();
        }

        return stockPricePo;
    }


    public List<Finance163StockHistoryPricePO> queryHistoryPrice(String code, String market){
        return null;
    }

    public List<Finance163StockHistoryFinancePO> queryHistoryFinance(String code, String market){
        return null;
    }

}
