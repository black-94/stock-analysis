package com.black.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.function.Function;

public class NetUtil {

    public static Logger root= LoggerFactory.getLogger(NetUtil.class);

    public static String get(String link,Object... params){
        return get(e->e,link,params);
    }

    public static String get(Function<String,String> fun,String link,Object... params){
        String u=String.format(link,params);
        for (int i = 0; i < 3; i++) {
            try {
                URL url=new URL(u);
                StringWriter writer=new StringWriter();
                try(InputStreamReader reader = new InputStreamReader(url.openConnection().getInputStream())){
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
