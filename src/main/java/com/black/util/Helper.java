package com.black.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
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
            if (str != null) {
                str = str.replace(",", "").replace(" ", "").replace("-", "");
            }
            return new BigDecimal(str);
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    public static String href2Code(String href) {
        try {
            href = StringUtils.remove(href, "http://quotes.money.163.com/");
            href = StringUtils.remove(href, ".html");
            return href.substring(1);
        } catch (Exception e) {
            log.error("",e);
            return "";
        }
    }

    public static BigDecimal parseTextNumber(String text) {
        String numStr = text.substring(0, text.length() - 1);
        BigDecimal num = decimalOf(numStr);
        if (text.endsWith("亿")) {
            return num.multiply(BigDecimal.valueOf(100000000));
        } else if (text.endsWith("万")) {
            return num.multiply(BigDecimal.valueOf(10000));
        }
        return num;
    }

    /**
     * pattern yyyy-MM-dd
     *
     * @param str
     * @return
     */
    public static Date parseDate(String str) {
        Instant ins = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(ins);
    }
}
