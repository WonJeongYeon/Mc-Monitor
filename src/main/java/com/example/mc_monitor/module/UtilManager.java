package com.example.mc_monitor.module;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilManager {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyyMMddHHmmss");

    public static LocalDateTime convertStringToLocalDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    public static String now() {
        return LocalDateTime.now().format(formatter);
    }
}
