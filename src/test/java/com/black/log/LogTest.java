package com.black.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogTest {

    public static void main(String[] args) {
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
