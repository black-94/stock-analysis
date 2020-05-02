package com.black.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.black.po.ErrorPo;
import com.black.po.StockFinancePo;
import com.black.repository.ErrorRepository;
import com.black.repository.StockFinanceRepository;
import com.black.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EastMoneyPullService {
    @Autowired
    ErrorRepository errorRepository;
    @Autowired
    StockFinanceRepository stockFinanceRepository;

    public void pullPriceData() {

    }

    public void pullFinanceData(){
        String url="http://data.eastmoney.com/bbsj/201912/yjbb.html";
        String resp = get(url);
        Pattern compile = Pattern.compile("dataurl: \"(.+)\"");
        Matcher matcher = compile.matcher(resp);
        String dataurl=null;
        if(matcher.find()){
            String group = matcher.group(1);
            dataurl = group.replace("{sortType}", "latestnoticedate")
                    .replace("{sortRule}", "-1")
                    .replace("{pageSize}", "50");
        }
        if(dataurl==null){
            errorRepository.save(ErrorPo.builder().error("解析数据错误,url:"+url+",resp:"+resp).build());
            return;
        }

        String reportDate = reportDate();
        String[] marketCodes={"058001001","058001002"};
        for (int i = 0; i < marketCodes.length; i++) {
            String marketCode=marketCodes[i];
            String params="&filter=(securitytypecode=%27{marketCode}%27)(reportdate=^{reportDate}^)".replace("{marketCode}",marketCode).replace("{reportDate}",reportDate);
            int page=1;
            do{
                String tmpUrl=dataurl;
                String jsname="a"+(new Random().nextInt(100));
                String origin="var {jsname}={pages:(tp),data: (x),font:(font)}";
                String jsparam=origin.replace("{jsname}",jsname).replace(" ","%20");
                tmpUrl=tmpUrl.replace(origin,jsparam).replace("{param}",params);
                String str = tmpUrl.replace("{page}", ""+page);
                String res = get(str);
                if(res==null){
                    break;
                }
                String data = res.replace("pages:", "\"pages\":")
                        .replace("data:", "\"data\":")
                        .replace("font:", "\"font\":")
                        .replace("var "+jsname+"=","");
                JSONObject json = null;
                try {
                    json=JSON.parseObject(data);
                } catch (Exception e) {
                    String msg = Helper.stack(e);
                    errorRepository.save(new ErrorPo(msg));
                }


                int pages = json.getIntValue("pages");
                JSONArray fontMapping = json.getJSONObject("font").getJSONArray("FontMapping");
                JSONArray dataArr = json.getJSONArray("data");
                List<StockFinancePo> financePos = parse(dataArr, fontMapping);
                for (StockFinancePo financePo : financePos) {
                    StockFinancePo po = stockFinanceRepository.findByCodeAndDate(financePo.getCode(), financePo.getDate());
                    if(po!=null){
                        financePo.setId(po.getId());
                    }
                    stockFinanceRepository.save(financePo);
                }

                if(page>=pages){
                    break;
                }
                ++page;
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {}
            }while (true);
        }
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
            String t = decode(d.toString(), mapping);
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

    public String decode(String str,JSONArray mapping){
        for (Object m : mapping) {
            JSONObject j= (JSONObject) m;
            String code = j.getString("code");
            int value = j.getIntValue("value");
            str=str.replace(code,value+"");
        }
        return str;
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
