package com.wora.citronix.harvest.domain.vo;

import java.time.LocalDate;
import java.time.Month;

public enum Season {
    SPRING, SUMMER, FALL, WINTER;

    public static Season fromDate(LocalDate date) {
        Month month = date.getMonth();
        int dayOfMonth = date.getDayOfMonth();

        return switch (month) {
            case MARCH -> (dayOfMonth >= 20) ? SPRING : WINTER;
            case APRIL, MAY -> SPRING;
            case JUNE -> (dayOfMonth >= 21) ? SUMMER : SPRING;
            case JULY, AUGUST -> SUMMER;
            case SEPTEMBER -> (dayOfMonth >= 23) ? FALL : SUMMER;
            case OCTOBER, NOVEMBER -> FALL;
            case DECEMBER -> (dayOfMonth >= 21) ? WINTER : FALL;
            case JANUARY, FEBRUARY -> WINTER;
        };
    }

    public boolean matches(LocalDate date) {
        return this == fromDate(date);
    }
}
