package com.black.repository;

import com.black.po.StockInfoPo;
import com.black.util.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
            String exchanger=e.child(0).nodeName();
            List<String> codes = e.select("ul li a").stream().map(a -> a.text()).filter(StringUtils::isNoneBlank)
                    .distinct().collect(Collectors.toList());
            codes.forEach(c->list.add(buildStockInfoPo(exchanger,c)));
        }
        return list;
    }

    private StockInfoPo buildStockInfoPo(String exchanger,String codeStr){
        String[] arr = StringUtils.split(codeStr, '(');
        String name=arr[0];
        String code=StringUtils.replace(arr[1],")","");
        return StockInfoPo.builder().exchanger(exchanger).name(name).code(code).build();
    }

}