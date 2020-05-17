package com.black.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    /**
     * pattern yyyy-MM-dd
     * @param str
     * @return
     */
    public static Date parseDate(String str){
        Instant ins = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(ins);
    }
}
