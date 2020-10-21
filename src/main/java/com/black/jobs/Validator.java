package com.black.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class Validator {
    static Logger validateLogger = LogManager.getLogger("root.validate.log");

    public void validateAllPrice() {
        Instant begin = LocalDate.of(1990, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = LocalDate.now().plus(1, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant();
        validatePriceIn(Date.from(begin),Date.from(end));
    }

    public void validatePriceIn(Date begin, Date end) {



    }

    public void validatePricePage(Date date) {

    }

    public void validatePrice(Date date) {

    }


}
