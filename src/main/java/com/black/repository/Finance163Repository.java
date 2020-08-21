package com.black.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.black.po.*;
import com.black.pojo.*;
import com.black.util.Helper;
import com.black.util.MarketParser;
import com.black.util.NetUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.black.util.Helper.decimalOf;

@Repository
public class Finance163Repository {

    public List<IpoStockPage> queryCodes(String year) {
        int page = 0;
        String url = "http://quotes.money.163.com/data/ipo/shangshi.html?reportdate=%s&page=%d&sort=faxingri&order=desc";
        List<IpoStockPage> list = Lists.newArrayList();
        do {
            String res = NetUtil.get(url, year, page);
            Document doc = Jsoup.parse(res);
            Elements trs = doc.select("#plate_performance tbody tr");
            for (Element tr : trs) {
                String code = tr.child(1).text();
                String name = tr.child(2).text();
                String date = tr.child(3).text();

                IpoStockPage ipoStockPage = IpoStockPage.builder().code(code).name(name).marketDay(date).marketYear(year).build();
                list.add(ipoStockPage);
            }
            if (trs.isEmpty() || !hasNextPage(doc)) {
                break;
            }
            page++;
        } while (true);

        return list;
    }

    public StockInfoPage queryInfov2(String code) {
        String url = "http://quotes.money.163.com/f10/gszl_%s.html";
        String res = NetUtil.get(url, code);
        Document doc = Jsoup.parse(res);
        Elements elements = doc.select(".table_bg001 .td_label");

        String name = "";
        String biz = "";
        String openDay = "";
        String marketDay = "";
        for (Element e : elements) {
            String text = e.text();
            String value = e.nextElementSibling().text();
            switch (text) {
                case "中文简称":
                    name = value;
                    break;
                case "主营业务":
                    biz = value;
                    break;
                case "上市日期":
                    marketDay = value;
                    break;
                case "成立日期":
                    openDay = value;
                    break;
                default:
                    break;
            }
        }

        StockInfoPage po = new StockInfoPage();
        po.setCode(code);
        po.setName(name);
        po.setBiz(biz);
        po.setOpenDay(openDay);
        po.setMarketDay(marketDay);
        return po;
    }

    public StockNumPage queryStockNum(String code) {
        String exchanger = MarketParser.parse(code).getKey();
        String key = (exchanger.contains("sz") ? 1 : 0) + code;
        String url = "http://quotes.money.163.com/%s.html";
        String res = NetUtil.get(url, key);
        Elements elements = Jsoup.parse(res).select(".corp_info.inner_box p");
        String marketDayStr = elements.get(8).text();
        String totalStr = elements.get(9).text();
        String numStr = elements.get(10).text();

        StockNumPage po = new StockNumPage();
        po.setCode(code);
//        po.setName();
//        po.setBiz();
//        po.setMarketDay();
        po.setTotal(totalStr);
        po.setCycle(numStr);
        po.setDate(Helper.formatDate(new Date()));

        return po;
    }

    public StockPricePage queryPriceV2(String code) {
        String exchanger = MarketParser.parse(code).getKey();
        String key = (exchanger.contains("sz") ? 1 : 0) + code;
        String url = "http://api.money.126.net/data/feed/%s,money.api?callback=a";
        String res = NetUtil.get(e -> e.substring(2, e.length() - 2), url, key);
        JSONObject json = JSON.parseObject(res).getJSONObject(key);
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
        date = StringUtils.replace(date, "/", "-");

        StockPricePage stockPricePo = new StockPricePage();
        stockPricePo.setCode(code);
        stockPricePo.setOpen(open);
        stockPricePo.setLastClose(yestclose);
        stockPricePo.setCur(price);
        stockPricePo.setHigh(high);
        stockPricePo.setLow(low);
        stockPricePo.setVolume(volume);
        stockPricePo.setAmount(turnover);
        stockPricePo.setPercent(percent);
        stockPricePo.setChange(updown);
        stockPricePo.setDate(date);

        return stockPricePo;
    }

    public List<StockPriceHistoryPage> queryHistoryPrice(String code, String year, String season) {
        List<StockPriceHistoryPage> list = new ArrayList<>();
        String url = "http://quotes.money.163.com/trade/lsjysj_%s.html?year=%s&season=%s";
        String res = NetUtil.get(url, code, year, season);
        Document doc = Jsoup.parse(res);
        Elements elements = doc.select(".table_bg001 tr");
        if (elements.size() < 2) {
            return list;
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
            String amplitude = tds.get(9).text();
            String exchange = tds.get(10).text();

            StockPriceHistoryPage stockPricePo = new StockPriceHistoryPage();
            stockPricePo.setCode(code);
            stockPricePo.setOpen(open);
            stockPricePo.setHigh(high);
            stockPricePo.setLow(low);
            stockPricePo.setClose(close);
            stockPricePo.setPercent(updown);
            stockPricePo.setChange(change);
            stockPricePo.setVolume(volume);
            stockPricePo.setAmount(amount);
            stockPricePo.setAmplitude(amplitude);
            stockPricePo.setExchange(exchange);
            stockPricePo.setDate(time);

            list.add(stockPricePo);
        }

        return list;
    }

    public List<StockFinancePage> queryFinance(String code) {
        String url = "http://quotes.money.163.com/f10/lrb_%s.html";
        String res = NetUtil.get(url, code);
        Document doc = Jsoup.parse(res);

        Element status = doc.selectFirst(".stock_detail .price");
        if (status != null && (status.text().equals("已退市") || status.text().equals("未上市"))) {
            return new ArrayList<>();
        }

        Elements elements = doc.select(".table_bg001.border_box.limit_sale.scr_table tr");
        Elements dates = elements.get(0).select("th");
        Elements incomes = elements.get(1).select("td");
        Elements profits = elements.get(40).select("td");

        if (dates.size() < 1) {
            return new ArrayList<>();
        }

        List<StockFinancePage> pos = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            String time = dates.get(i).text();
            String income = incomes.get(i).text();
            String profit = profits.get(i).text();

            StockFinancePage po = new StockFinancePage();
            po.setCode(code);
            po.setIncome(income);
            po.setProfit(profit);
            po.setReportDay(time);

            pos.add(po);
        }

        return pos;
    }

    public List<StockInfoPo> queryAllCodes() {
        String url = "http://quotes.money.163.com/data/ipo/shangshi.html";
        String res = NetUtil.get(url);
        Document doc = Jsoup.parse(res);
        List<String> years = parseReportYear(doc);
        List<String> allCodes = Lists.newArrayList();
        for (String year : years) {
            int page = 0;
            do {
                url = "http://quotes.money.163.com/data/ipo/shangshi.html?reportdate=%s&page=%d&sort=faxingri&order=desc";
                res = NetUtil.get(url, year, page);
                doc = Jsoup.parse(res);
                List<String> tmp = parseCodes(doc);
                allCodes.addAll(tmp);
                if (tmp.isEmpty() || !hasNextPage(doc)) {
                    break;
                }
                page++;
            } while (true);
        }

        return allCodes.stream().distinct().map(this::buildStockInfo).collect(Collectors.toList());
    }

    private List<String> parseReportYear(Document doc) {
        Elements elements = doc.select("#fn_rp_title_list a");
        return elements.stream().map(e -> e.text()).distinct().collect(Collectors.toList());
    }

    private boolean hasNextPage(Document doc) {
        String text = doc.selectFirst(".mod_pages .current").nextElementSibling().text();
        return !"下一页".equals(text);
    }

    private List<String> parseCodes(Document doc) {
        Elements trs = doc.select("#plate_performance tbody tr");
        return trs.stream().map(tr -> tr.child(1).text()).collect(Collectors.toList());
    }

    private StockInfoPo buildStockInfo(String code) {
        Pair<String, String> pair = MarketParser.parse(code);
        return StockInfoPo.builder().code(code).exchanger(pair.getKey()).subExchanger(pair.getValue())
                .name("").biz("").openDay(new Date(0)).marketDay(new Date(0)).build();
    }

    public List<StockInfoPo> queryTodayCodes() {
        int page = 0;
        int year = LocalDate.now().getYear();
        List<String> codes = Lists.newArrayList();
        do {
            String url = "http://quotes.money.163.com/data/ipo/shangshi.html?reportdate=%s&page=%d&sort=faxingri&order=desc";
            String res = NetUtil.get(url, year, page);
            Document doc = Jsoup.parse(res);
            List<String> tmp = parseCodes(doc);
            codes.addAll(tmp);
            if (tmp.isEmpty() || !hasNextPage(doc)) {
                break;
            }
            page++;
        } while (true);
        return codes.stream().distinct().map(this::buildStockInfo).collect(Collectors.toList());
    }

    public Finance163StockInfoPO queryInfo(String code) {
        String url = "http://quotes.money.163.com/f10/gszl_%s.html";
        String res = NetUtil.get(url, code);
        Document doc = Jsoup.parse(res);
        Elements elements = doc.select(".table_bg001 .td_label");

        String name = "";
        String biz = "";
        String openDay = "";
        String marketDay = "";
        for (Element e : elements) {
            String text = e.text();
            String value = e.nextElementSibling().text();
            switch (text) {
                case "中文简称":
                    name = value;
                    break;
                case "主营业务":
                    biz = value;
                    break;
                case "上市日期":
                    marketDay = value;
                    break;
                case "成立日期":
                    openDay = value;
                    break;
                default:
                    break;
            }
        }

        Finance163StockInfoPO po = new Finance163StockInfoPO();
        po.setCode(code);
        po.setName(name);
        po.setBiz(biz);
        po.setOpenDay(openDay);
        po.setMarketDay(marketDay);
        return po;
    }

    public Finance163StockPricePO queryCurPrice(String code, String exchanger) {
        String key = (exchanger.contains("sz") ? 1 : 0) + code;
        String url = "http://api.money.126.net/data/feed/%s,money.api?callback=a";
        String res = NetUtil.get(e -> e.substring(2, e.length() - 2), url, key);
        JSONObject json = JSON.parseObject(res).getJSONObject(key);
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
        date = StringUtils.replace(date, "/", "-");

        url = "http://quotes.money.163.com/%s.html";
        res = NetUtil.get(url, key);
        Elements elements = Jsoup.parse(res).select(".corp_info.inner_box p");
        String marketDayStr = elements.get(8).text();
        marketDayStr = Helper.truncateAfter(marketDayStr, "上市：");
        String totalStr = elements.get(9).text();
        totalStr = Helper.truncateAfter(totalStr, "本：");
        totalStr = StringUtils.remove(totalStr, "股");
        String numStr = elements.get(10).text();
        numStr = Helper.truncateAfter(numStr, "本：");
        numStr = StringUtils.remove(numStr, "股");

        Finance163StockPricePO stockPricePo = new Finance163StockPricePO();
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
        stockPricePo.setTotal(totalStr);
        stockPricePo.setNum(numStr);
        stockPricePo.setMarketDay(marketDayStr);
        stockPricePo.setDate(date);

        return stockPricePo;
    }

    public List<Finance163StockHistoryPricePO> queryHistoryPrice(String code, Date marketDay) {
        int year = LocalDate.now().getYear();
        int season = (LocalDate.now().getMonthValue() + 2) / 3;
        int marketYear = year - 1;
        int marketSeason = 1;

        if (new Date(0).compareTo(marketDay) < 0) {
            ZonedDateTime date = marketDay.toInstant().atZone(ZoneId.systemDefault());
            marketYear = date.getYear();
            marketSeason = (date.getMonthValue() + 2) / 3;
        }

        List<Finance163StockHistoryPricePO> list = new ArrayList<>();
        for (int i = marketYear; i <= year; i++) {
            int begin = 1, end = 5;
            if (i == marketYear) {
                begin = marketSeason;
            }
            if (i == year) {
                end = season + 1;
            }

            for (int j = begin; j < end; ++j) {
                String url = "http://quotes.money.163.com/trade/lsjysj_%s.html?year=%d&season=%d";
                String res = NetUtil.get(url, code, i, j);
                Document doc = Jsoup.parse(res);
                Elements elements = doc.select(".table_bg001 tr");
                if (elements.size() < 2) {
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
                    String amplitude = tds.get(9).text();
                    String exchange = tds.get(10).text();

                    Finance163StockHistoryPricePO stockPricePo = new Finance163StockHistoryPricePO();
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

    public List<Finance163StockHistoryFinancePO> queryHistoryFinance(String code) {
        String url = "http://quotes.money.163.com/f10/zycwzb_%s,season.html";
        String res = NetUtil.get(url, code);
        Document doc = Jsoup.parse(res);

        Element status = doc.selectFirst(".stock_detail .price");
        if (status != null && (status.text().equals("已退市") || status.text().equals("未上市"))) {
            return new ArrayList<>();
        }

        Elements elements = doc.select(".table_bg001.border_box.limit_sale.scr_table tr");
        Elements dates = elements.get(0).select("th");
        Elements incomes = elements.get(4).select("td");
        Elements profits = elements.get(5).select("td");

        if (dates.size() < 1) {
            return new ArrayList<>();
        }

        List<Finance163StockHistoryFinancePO> pos = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            String time = dates.get(i).text();
            String income = incomes.get(i).text();
            String profit = profits.get(i).text();

            Finance163StockHistoryFinancePO po = new Finance163StockHistoryFinancePO();
            po.setCode(code);
            po.setDate(time);
            po.setIncome(income);
            po.setProfit(profit);

            pos.add(po);
        }

        return pos;
    }

    public List<Finance163FundPricePO> fundList() {
        //小于10亿或最后一页或小于size
        int pageNo = 0;
        int pageSize = 1000;
        BigDecimal billion = BigDecimal.valueOf(1000000000L);
        String url = "http://quotes.money.163.com/fn/service/netvalue.php?page=%s&sort=ZJZC&order=desc&count=%s";

        List<Finance163FundPricePO> ret = new ArrayList<>();
        do {
            String res = NetUtil.get(String.format(url, pageNo, pageSize));
            JSONArray list = JSON.parseObject(res).getJSONArray("list");

            for (Object o : list) {
                JSONObject json = (JSONObject) o;

                Finance163FundPricePO po = new Finance163FundPricePO();
                po.setFundCode(json.getString("SYMBOL"));
                po.setFundName(json.getString("SNAME"));
                po.setUnit(json.getString("NAV"));
                po.setRatio(json.getString("PCHG"));
                po.setAmount(json.getString("ZJZC"));
                po.setDate(json.getString("PUBLISHDATE"));
                po.setMarketDate(json.getString("OFPROFILE8"));
                po.setType(json.getString("TYPENAME3"));
                po.setM1ret(json.getString("M1RETRUN"));
                po.setM3ret(json.getString("M3RETRUN"));
                po.setM6ret(json.getString("M6RETRUN"));
                po.setM12ret(json.getString("M12RETRUN"));

                ret.add(po);
            }

            if (CollectionUtils.isEmpty(list) || list.size() < pageSize) {
                break;
            }

            String lastAmout = ret.get(ret.size() - 1).getAmount();
            if (decimalOf(lastAmout).compareTo(billion) < 0) {
                break;
            }

            ++pageNo;
        } while (true);

        return ret;
    }

    public List<Finance163FundStockPO> fundStockList(String fundCode) {
        String url = "http://quotes.money.163.com/fund/cgmx_%s.html";
        String res = NetUtil.get(String.format(url, fundCode));
        Elements elements = Jsoup.parse(res).select("#fn_fund_owner_01 .fn_cm_table.fn_fund_rank tbody tr");
        String reportDate = Jsoup.parse(res).selectFirst(".fn_fund_selector option[selected]").text();

        List<Finance163FundStockPO> list = Lists.newArrayList();
        for (Element element : elements) {
            Elements tds = element.select("td");
            String href = tds.get(0).select("a").attr("href");
            String code = Helper.href2Code(href);
            if (StringUtils.isBlank(code)) {
                continue;
            }

            String stockNums = tds.get(1).text();
            String stockAmount = tds.get(2).text();
            String stockRatio = tds.get(3).text();

            Finance163FundStockPO po = new Finance163FundStockPO();
            po.setFundCode(fundCode);
            po.setStockCode(code);
            po.setStockNums(Helper.parseTextNumber(stockNums));
            po.setStockAmount(Helper.parseTextNumber(stockAmount));
            po.setStockRatio(Helper.parseTextNumber(stockRatio));
            po.setDate(reportDate);
            list.add(po);
        }

        return list;
    }

    public List<Finance163FundPricePO> fundHistoryPrice(String fundCode, String marketDate) {
        String today = LocalDate.now().toString();
        String beginDay = marketDate;
        String url = "http://quotes.money.163.com/fund/jzzs_%s_%s.html?start=%s&end=%s&sort=TDATE&order=desc";
        int pageNo = 0;

        List<Finance163FundPricePO> pos = new ArrayList<>();
        do {
            String res = NetUtil.get(String.format(url, fundCode, pageNo, beginDay, today));
            Elements elements = Jsoup.parse(res).select(".fn_cm_table tbody tr");
            for (Element element : elements) {
                Elements tds = element.select("td");
                String date = tds.get(0).text();
                String unit = tds.get(1).text();
                String ratio = tds.get(3).text();

                Finance163FundPricePO po = new Finance163FundPricePO();
                po.setFundCode(fundCode);
                po.setDate(date);
                po.setRatio(StringUtils.remove(ratio, "%"));
                po.setUnit(unit);

                pos.add(po);
            }

            ++pageNo;
            if (elements.size() < 60) {
                break;
            }
        } while (true);

        return pos;
    }

    public List<Finance163FundStockPO> fundHistoryStock(String fundCode) {
        String url = "http://quotes.money.163.com/fund/cgmx_%s.html";
        String res = NetUtil.get(String.format(url, fundCode));
        List<String> reportDates = Jsoup.parse(res).select(".fn_fund_selector option").stream().map(e -> e.text()).collect(Collectors.toList());
        List<Finance163FundStockPO> list = Lists.newArrayList();

        for (String reportDate : reportDates) {
            String ret = NetUtil.get(String.format(url + "?reportDate=%s", fundCode, reportDate));
            Elements elements = Jsoup.parse(ret).select("#fn_fund_owner_01 .fn_cm_table.fn_fund_rank tbody tr");
            for (Element element : elements) {
                Elements tds = element.select("td");
                String href = tds.get(0).select("a").attr("href");
                String code = Helper.href2Code(href);
                if (StringUtils.isBlank(code)) {
                    continue;
                }

                String stockNums = tds.get(1).text();
                String stockAmount = tds.get(2).text();
                String stockRatio = tds.get(3).text();

                Finance163FundStockPO po = new Finance163FundStockPO();
                po.setFundCode(fundCode);
                po.setStockCode(code);
                po.setStockNums(Helper.parseTextNumber(stockNums));
                po.setStockAmount(Helper.parseTextNumber(stockAmount));
                po.setStockRatio(Helper.parseTextNumber(stockRatio));
                po.setDate(reportDate);
                list.add(po);
            }
        }

        return list;
    }

}
