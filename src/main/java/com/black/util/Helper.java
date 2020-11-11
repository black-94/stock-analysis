package com.black.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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

    public static BigDecimal safeDivide(BigDecimal divided, BigDecimal divide) {
        try {
            return divided.divide(divide, 6, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("", e);
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

    public static String href2FundCode(String href) {
        try {
            href = StringUtils.remove(href, "http://quotes.fund.163.com/html/");
            href = StringUtils.remove(href, ".html");
            return href;
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

    public static String truncateAfter(String str, String tag) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (StringUtils.isBlank(tag)) {
            return str;
        }
        int i = str.indexOf(tag);
        if (i < 0) {
            return str;
        }
        return str.substring(i + tag.length());
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

    public static final FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");

    public static String formatDate(Date date) {
        return format.format(date);
    }

    public static Date datePlus(Date date, int diff, ChronoUnit unit) {
        ChronoUnit[] units = {ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.WEEKS, ChronoUnit.DAYS};
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        if (Arrays.asList(units).contains(unit)) {
            Instant instant = zonedDateTime.toLocalDate().plus(diff, unit).atStartOfDay(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        } else {
            Instant instant = zonedDateTime.plus(diff, unit).toInstant();
            return Date.from(instant);
        }
    }

    public static Date findRecentReportDay(Date datetime) {
        LocalDate now = datetime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = (now.getMonthValue() - 1) / 3 * 3 + 1;
        Instant reportDay = LocalDate.of(now.getYear(), month, 1).atStartOfDay(ZoneId.systemDefault()).plusDays(-1).toInstant();
        return Date.from(reportDay);
    }

    public static BigDecimal calculateIncrease(BigDecimal cur, BigDecimal pre) {
        if (cur == null || pre == null) {
            return null;
        }
        if (cur.compareTo(pre) >= 0) {
            if (pre.compareTo(BigDecimal.ZERO) < 0) {
                return Helper.safeDivide(cur, pre).abs().add(BigDecimal.ONE);
            }
            return Helper.safeDivide(cur, pre).abs().subtract(BigDecimal.ONE);
        }
        if (pre.compareTo(BigDecimal.ZERO) < 0) {
            return Helper.safeDivide(cur, pre).abs().negate().add(BigDecimal.ONE);
        }
        return Helper.safeDivide(cur, pre).abs().negate().subtract(BigDecimal.ONE);
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalTime localTime) {
        return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
