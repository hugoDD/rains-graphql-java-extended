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
package com.extended.graphql.scalars.datetime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.extended.graphql.scalars.util.DateTimeHelper.DATE_FORMATTERS;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
public class LocalDateTimeConverter {

    private boolean zoneConversionEnabled;

    LocalDateTimeConverter(boolean zoneConversionEnabled) {
        this.zoneConversionEnabled = zoneConversionEnabled;
    }

    // ISO_8601
    String toISOString(LocalDateTime dateTime, String pattern) {
        Objects.requireNonNull(dateTime, "dateTime");
        if (Objects.nonNull(pattern)) {
            return DateTimeFormatter.ofPattern(pattern).format(ZonedDateTime.of(toUTC(dateTime), ZoneOffset.UTC));
        }

        return DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.of(toUTC(dateTime), ZoneOffset.UTC));
    }

    LocalDateTime parseDate(String date) {
        Objects.requireNonNull(date, "date");

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                // equals ISO_LOCAL_DATE
                if (formatter.equals(DATE_FORMATTERS.get(2))) {
                    LocalDate localDate = LocalDate.parse(date, formatter);

                    return localDate.atStartOfDay();
                } else {
                    LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
                    return fromUTC(dateTime);
                }
            } catch (java.time.format.DateTimeParseException ignored) {
            }
        }

        return null;
    }

    private LocalDateTime convert(LocalDateTime dateTime, ZoneId from, ZoneId to) {
        if (zoneConversionEnabled) {
            return dateTime.atZone(from).withZoneSameInstant(to).toLocalDateTime();
        }
        return dateTime;
    }

    private LocalDateTime fromUTC(LocalDateTime dateTime) {
        return convert(dateTime, ZoneOffset.UTC, ZoneId.systemDefault());
    }

    private LocalDateTime toUTC(LocalDateTime dateTime) {
        return convert(dateTime, ZoneId.systemDefault(), ZoneOffset.UTC);
    }

}
