package com.black.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class Helper {
    public static String stack(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static BigDecimal decimalOf(String str) {
        try {
            if (StringUtils.isBlank(str)) {
                return BigDecimal.ZERO;
            }
            boolean negative = false;
            if (str.startsWith("-")) {
                negative = true;
            }
            str = str.replace(",", "").replace(" ", "").replace("-", "");
            BigDecimal res = new BigDecimal(str);
            return negative ? res.negate() : res;
        } catch (Exception e) {
            log.error("", e);
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal safeDivide(BigDecimal divided,BigDecimal divide){
        try {
            return divided.divide(divide,6, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("",e);
            return BigDecimal.ZERO;
        }
    }

    public static String href2Code(String href) {
        try {
            href = StringUtils.remove(href, "http://quotes.money.163.com/");
            href = StringUtils.remove(href, ".html");
            return href.substring(1);
        } catch (Exception e) {
            log.error("", e);
            return "";
        }
    }

    public static BigDecimal parseTextNumber(String text) {
        if (StringUtils.isBlank(text)) {
            return BigDecimal.ZERO;
        }

        String numStr = text.substring(0, text.length() - 1);
        if (text.endsWith("亿")) {
            BigDecimal num = decimalOf(numStr);
            return num.multiply(BigDecimal.valueOf(100000000));
        } else if (text.endsWith("万")) {
            BigDecimal num = decimalOf(numStr);
            return num.multiply(BigDecimal.valueOf(10000));
        }
        return decimalOf(text);
    }

    public static String truncateAfter(String str,String tag){
        if(StringUtils.isBlank(str)){
            return "";
        }
        if(StringUtils.isBlank(tag)){
            return str;
        }
        int i = str.indexOf(tag);
        if(i<0){
            return str;
        }
        return str.substring(i+tag.length());
    }

    /**
     * pattern yyyy-MM-dd
     *
     * @param str
     * @return
     */
    public static Date parseDate(String str) {
        try {
            Instant ins = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant();
            return Date.from(ins);
        } catch (Exception e) {
            return new Date(0);
        }
    }
}
