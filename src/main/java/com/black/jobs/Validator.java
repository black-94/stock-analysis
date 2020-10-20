package com.black.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Validator {
    static Logger validateLogger = LogManager.getLogger("root.validate");

    static {
        validateLogger.info("validate begin ---- ");
    }

    @Scheduled(cron = "* * * * * ?")
    public void validateAllPrice() {
        validateLogger.info("validate begin ---- ");
//        Instant begin = LocalDate.of(1990, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
//        Instant end = LocalDate.now().plus(1, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant();
//        validatePriceIn(Date.from(begin),Date.from(end));
    }


    public void validatePriceIn(Date begin, Date end) {


    }

    public void validatePricePage(Date date) {

    }

    public void validatePrice(Date date) {

    }


}
