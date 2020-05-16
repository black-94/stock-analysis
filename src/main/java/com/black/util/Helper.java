package com.black.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;

public class Helper {
    public static String stack(Throwable e){
        StringWriter stringWriter=new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static BigDecimal decimalOf(String str){
        try {
            if(str!=null){
                str=str.replace(",","").replace(" ","").replace("-","");
            }
            return new BigDecimal(str);
        } catch (Exception e) {
            return null;
        }
    }
}
