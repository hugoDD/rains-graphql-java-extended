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
package com.extended.graphql.sample.schema;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
@Component
public class Query implements GraphQLQueryResolver {

    private static String[] TITLES = new String[]{
            "Daoguang Emperor",
            "Xianfeng Emperor",
            "Tongzhi Emperor",
            "Guangxu Emperor",
            "Xuantong Emperor"
    };

    private static String[] NAMES = new String[]{
            "旻寧",
            "奕詝",
            "載淳",
            "載湉",
            "溥儀"
    };

    private static LocalDate[] REIGN_STARTS = new LocalDate[]{
            LocalDate.of(1820, Month.OCTOBER, 3),
            LocalDate.of(1850, Month.MARCH, 9),
            LocalDate.of(1861, Month.NOVEMBER, 11),
            LocalDate.of(1875, Month.FEBRUARY, 25),
            LocalDate.of(1908, Month.NOVEMBER, 14)
    };

    private static LocalDate[] REIGN_STOP = new LocalDate[]{
            LocalDate.of(1850, Month.FEBRUARY, 25),
            LocalDate.of(1861, Month.AUGUST, 22),
            LocalDate.of(1875, Month.JANUARY, 12),
            LocalDate.of(1908, Month.NOVEMBER, 14),
            LocalDate.of(1912, Month.FEBRUARY, 12)
    };

    public List<EmperorType> getEmperors() {
        List<EmperorType> result = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            result.add(
                    new EmperorType(NAMES[i], TITLES[i], REIGN_STARTS[i], REIGN_STOP[i])
            );
        }

        return result;
    }

    public Date getNow() {
        return new Date();
    }

    public LocalDate getYesterday() {
        return LocalDate.now().minusDays(1);
    }

    public LocalDateTime getTomorrowMidnight() {
        return LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);
    }

    public LocalTime getNoonTime() {
        return LocalTime.NOON;
    }

    public Node getRoot() {
        return Tree.root();
    }


    public Boolean hired(List<Application> applications) {
        System.out.println(applications.size());
        return true;
    }

}
