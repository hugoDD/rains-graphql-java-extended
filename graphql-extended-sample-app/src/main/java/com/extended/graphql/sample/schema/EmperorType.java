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

import java.time.LocalDate;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
public class EmperorType {

    private String givenName;
    private String title;
    private LocalDate reignStart;
    private LocalDate reignStop;

    public EmperorType(String givenName,
                       String title,
                       LocalDate reignStart,
                       LocalDate reignStop) {
        this.givenName = givenName;
        this.title = title;
        this.reignStart = reignStart;
        this.reignStop = reignStop;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReignStart() {
        return reignStart;
    }

    public LocalDate getReignStop() {
        return reignStop;
    }

    @Deprecated
    public String getName() {
        return getGivenName();
    }

}
