package com.black.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneId;

public class LogTest {

    public static void main(String[] args) {
        ZoneId of = ZoneId.of("UTC+8");
        System.out.println(of);
        System.out.println(ZoneId.systemDefault());
        Logger logger1 = LogManager.getLogger("a");
        Logger logger2 = LogManager.getLogger("a.b");
        Logger logger3 = LogManager.getLogger("a.b.c");
        Logger logger4 = LogManager.getLogger("d.e");
        logger1.info("logger1");
        logger2.info("logger2");
        logger3.info("logger3");
        logger4.info("logger4");
    }

}
