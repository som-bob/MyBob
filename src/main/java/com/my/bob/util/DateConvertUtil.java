package com.my.bob.util;

import io.micrometer.common.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConvertUtil {

    public static String convertDateToString(LocalDateTime localDateTime, String format){
        if(localDateTime == null || StringUtils.isEmpty(format)) {
            return "";
        }

        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * String 형태의 날짜를 LocalDateTime 형태로 변경한다
     * @param dateStr 날짜 String
     * @param format 날짜 format. 예를 들어 "yyyy-MM-dd"
     * @return 해당 날짜의 0시 0분
     */
    public static LocalDateTime convertStringToDate(String dateStr, String format) {
        if(StringUtils.isEmpty(dateStr) || StringUtils.isEmpty(format)) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(dateStr, formatter);

        return localDate.atStartOfDay();
    }
}
