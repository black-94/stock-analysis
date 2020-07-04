package com.black.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MarketParser {
    static final Map<String,String> code2Market;
    static {
        Map<String,String> map=new HashMap<>();
        map.put("sz.000","深A");
        map.put("sz.200","深B");
        map.put("sz.002","深中小");
        map.put("sz.300","深创");
        map.put("sh.601","沪A");
        map.put("sh.602","沪A");
        map.put("sh.603","沪A");
        map.put("sh.900","沪B");
        map.put("sh.688","沪科创");
        code2Market=Collections.unmodifiableMap(map);
    }

    public static String parse(String exchanger,String stockCode){
        String str = exchanger + "." + stockCode;
        str=str.substring(0,6);
        return code2Market.getOrDefault(str,"-");
    }
}
