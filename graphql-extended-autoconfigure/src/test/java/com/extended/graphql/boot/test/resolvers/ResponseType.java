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
package com.extended.graphql.boot.test.resolvers;

import java.time.*;
import java.util.Date;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
public class ResponseType {

    private Date date;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private LocalTime localTime;
    private OffsetDateTime offsetDateTime;

    public ResponseType() {
        date = new Date(1499667166754L);
        localDate = LocalDate.of(2017, 1, 1);
        localTime = LocalTime.MIDNIGHT;
        localDateTime = LocalDateTime.of(localDate, localTime);
        offsetDateTime = OffsetDateTime.of(localDate, localTime, ZoneOffset.UTC);
    }

    public Date getDate() {
        return date;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public OffsetDateTime getOffsetDateTime() {
        return offsetDateTime;
    }
}
