package com.black.repository;

import com.black.po.StockInfoPo;
import com.black.util.MarketParser;
import com.black.util.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EastMoneyRepository {

    public List<StockInfoPo> queryAllStockCode(){
        String url="http://quote.eastmoney.com/stock_list.html";
        String res = NetUtil.get(url);
        Elements uls = Jsoup.parse(res).select("#quotesearch ul");
        List<StockInfoPo> list=new ArrayList<>();
        for (Element e : uls) {
            String exchanger = e.previousElementSibling().selectFirst("a").attr("name");
            List<String> codes = e.select("ul li a").stream().map(a -> a.attr("href")).filter(StringUtils::isNoneBlank)
                    .distinct().collect(Collectors.toList());
            codes.forEach(c->list.add(buildStockInfoPo(c,exchanger)));
        }
        return list;
    }

    private StockInfoPo buildStockInfoPo(String link, String exchanger){
        link=StringUtils.remove(link,"http://quote.eastmoney.com/");
        link=StringUtils.remove(link,".html");
        link=link.substring(2);
        String sub = MarketParser.parse(exchanger, link);
        return StockInfoPo.builder().code(link).exchanger(exchanger).subExchanger(sub).build();
    }

}
