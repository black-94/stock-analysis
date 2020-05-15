package com.black.repository;

import com.black.po.*;
import com.black.util.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class Finance163Repository {

    public Finance163StockInfoPO queryInfo(String code, String exchanger){
//        String url=String.format("http://quotes.money.163.com/f10/gszl_%s.html",code);
//        String res = get(url);
//        if(res==null){
//        }
//        try {
//            Document doc = Jsoup.parse(res);
//            Elements elements = doc.select(".table_bg001 .td_label");
//            StockInfoPo po=new StockInfoPo();
//            String name="";
//            String business="";
//            String openDay="";
//            String marketDay="";
//
//            for (Element e : elements) {
//                String text = e.text();
//                String value = e.nextElementSibling().text();
//                switch (text){
//                    case "中文简称":name=value;break;
//                    case "主营业务":business=value;break;
//                    case "上市日期":marketDay=value;break;
//                    case "成立日期":openDay=value;break;
//                    default:break;
//                }
//            }
//
//            po.setCode(code);
//            po.setExchange(market);
//            po.setName(name);
//            po.setBusiness(business);
//            po.setOpenDay(openDay);
//            po.setMarketDay(marketDay);
//            po.setInfoInit(1);
//            po.setPriceComplete(0);
//            po.setFinanceComplete(0);
//            po.setCreateTime(new Date());
//            po.setUpdateTime(new Date());
//
//            stockInfoRepository.save(po);
//        } catch (Exception e) {
//            errorRepository.save(ErrorPo.builder().type(e.getClass().getName()).msg(e.getMessage()).stack(Helper.stack(e)).build());
//        }
        return null;
    }

    public Finance163StockPricePO queryCurPrice(String code, String market){
        return null;
    }

    public List<Finance163StockHistoryPricePO> queryHistoryPrice(String code, String market){
        return null;
    }

    public List<Finance163StockHistoryFinancePO> queryHistoryFinance(String code, String market){
        return null;
    }

}
