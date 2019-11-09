package com.injury.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDate {
    public static String DATE_FORMAT_INPUT = "yyyyMMdd";
    public static String DATE_FORMAT_OUTPUT = "yyyy-MM-dd";

    public static void main(String[] args) {
        System.out.println(formatted(convert("20190211")));
    }

    public static String formatted(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_OUTPUT));
    }

    public static LocalDate convert(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DATE_FORMAT_INPUT));
    }
}