package com.black.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MarketParser {
    static final Map<String, String> code2Market;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("000", "sz.深A");
        map.put("001", "sz.深A");
        map.put("002", "sz.深中小");
        map.put("003", "sz.深A");
        map.put("200", "sz.深B");
        map.put("300", "sz.深创");
        map.put("600", "sh.沪A");
        map.put("601", "sh.沪A");
        map.put("602", "sh.沪A");
        map.put("603", "sh.沪A");
        map.put("605", "sh.沪A");
        map.put("900", "sh.沪B");
        map.put("688", "sh.沪科创");
        code2Market = Collections.unmodifiableMap(map);
    }

    public static Pair<String, String> parse(String stockCode) {
        String str = stockCode.substring(0, 3);
        String market = code2Market.get(str);
        if (market != null) {
            return Pair.of(market.substring(0, 2), market.substring(3));
        } else {
            return Pair.of("-", "-");
        }
    }
}
