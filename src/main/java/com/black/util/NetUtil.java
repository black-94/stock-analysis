package com.black.util;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

public class NetUtil {

    public static Logger root= LoggerFactory.getLogger(NetUtil.class);

    public static RateLimiter rateLimiter = RateLimiter.create(50000000);

    public static String get(String link,Object... params){
        return get(e->e,link,params);
    }

    public static String get(Function<String,String> fun,String link,Object... params){
        String u=String.format(link,params);
        rateLimiter.acquire();
        for (int i = 0; i < 1; i++) {
            try {
                URL url=new URL(u);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","charset=utf-8");
                StringWriter writer=new StringWriter();
                try(InputStreamReader reader = new InputStreamReader(conn.getInputStream())){
                    reader.transferTo(writer);
                }
                String res = writer.toString();
                return fun.apply(res);
            } catch (Exception e) {
                root.error("request fail,url:"+u,e);
            }
        }
        return null;
    }

}
