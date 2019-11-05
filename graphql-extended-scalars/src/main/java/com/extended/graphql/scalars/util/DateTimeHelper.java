/*
 *  Copyright 2019 hugoDD
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.extended.graphql.scalars.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
public class DateTimeHelper {

    public static final CopyOnWriteArrayList<DateTimeFormatter> DATE_FORMATTERS = new CopyOnWriteArrayList<>();

    static {
        DATE_FORMATTERS.add(DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC));
        DATE_FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneOffset.UTC));
        DATE_FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneOffset.UTC));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ISO_8601
    public static String toISOString(LocalDateTime dateTime, String pattern) {
        Objects.requireNonNull(dateTime, "dateTime");
        if (Objects.nonNull(pattern)) {
            return DateTimeFormatter.ofPattern(pattern).format(ZonedDateTime.of(dateTime, ZoneOffset.UTC));
        }

        return DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.of(dateTime, ZoneOffset.UTC));
    }

    public static String toISOString(LocalDate date, String pattern) {
        Objects.requireNonNull(date, "date");
        if (Objects.nonNull(pattern)) {
            return DateTimeFormatter.ofPattern(pattern).format(date);
        }
        return DateTimeFormatter.ISO_LOCAL_DATE.format(date);
    }

    public static String toISOString(LocalTime time, String pattern) {
        Objects.requireNonNull(time, "time");
        if (Objects.nonNull(pattern)) {
            return DateTimeFormatter.ofPattern(pattern).format(time);
        }
        return DateTimeFormatter.ISO_LOCAL_TIME.format(time);
    }

    public static String toISOString(Date date, String pattern) {
        Objects.requireNonNull(date, "date");
        if (Objects.nonNull(pattern)) {
            return toISOString(toLocalDateTime(date), pattern);
        }
        return toISOString(toLocalDateTime(date), pattern);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        Objects.requireNonNull(date, "date");

        return date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    public static Date toDate(LocalDate date) {
        Objects.requireNonNull(date, "date");

        return toDate(date.atStartOfDay());
    }

    public static Date toDate(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime");

        return Date.from(dateTime.atZone(ZoneOffset.UTC).toInstant());
    }

    public static LocalDateTime parseDate(String date) {
        Objects.requireNonNull(date, "date");

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                // equals ISO_LOCAL_DATE
                if (formatter.equals(DATE_FORMATTERS.get(2))) {
                    LocalDate localDate = LocalDate.parse(date, formatter);

                    return localDate.atStartOfDay();
                } else {
                    return LocalDateTime.parse(date, formatter);
                }
            } catch (java.time.format.DateTimeParseException ignored) {
            }
        }

        return null;
    }

    public static Date createDate(int year, int month, int day) {
        return createDate(year, month, day, 0, 0, 0, 0);
    }

    public static Date createDate(int year, int month, int day, int hours, int min, int sec) {
        return createDate(year, month, day, hours, min, sec, 0);
    }

    public static Date createDate(int year, int month, int day, int hours, int min, int sec, int millis) {
        long nanos = TimeUnit.MILLISECONDS.toNanos(millis);
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hours, min, sec, (int) nanos);
        return DateTimeHelper.toDate(localDateTime);
    }

}
