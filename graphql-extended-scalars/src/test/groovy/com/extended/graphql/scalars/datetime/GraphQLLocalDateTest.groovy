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
package com.extended.graphql.scalars.datetime

import graphql.language.StringValue
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.ZoneOffset

/**
 * @author <ahref='mailto:280555235@qq.com' > hugoDD</a>
 */
class GraphQLLocalDateTest extends Specification {

    @Unroll
    def "Date parse literal #literal.value as #result"() {
        expect:
        new GraphQLLocalDate().getCoercing().parseLiteral(literal) == result

        where:
        literal                                | result
        new StringValue('2017-07-09')          | LocalDate.of(2017, 7, 9)
        new StringValue('2017-07-09 23:59:59') | LocalDate.of(2017, 7, 9)
    }

    @Unroll
    def "Date returns null for invalid #literal"() {
        expect:
        new GraphQLLocalDate().getCoercing().parseLiteral(literal) == null

        where:
        literal                       | _
        new StringValue("not a date") | _
    }

    @Unroll
    def "Date serialize #value into #result (#result.class)"() {
        expect:
        new GraphQLLocalDate().getCoercing().serialize(value) == result

        where:
        value                    | result
        LocalDate.of(2017, 7, 9) | '2017-07-09'
    }

    @Unroll
    def "serialize throws exception for invalid input #value"() {
        when:
        new GraphQLLocalDate().getCoercing().serialize(value)
        then:
        thrown(CoercingSerializeException)

        where:
        value        | _
        ''           | _
        'not a date' | _
        new Object() | _
    }

    @Unroll
    def "Date parse #value into #result (#result.class)"() {
        expect:
        new GraphQLLocalDate().getCoercing().parseValue(value) == result

        where:
        value        | result
        '2017-07-09' | LocalDate.of(2017, 7, 9)
    }

    @Unroll
    def "Date parse #value into #result (#result.class) using zone conversion"() {
        when:
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.ofHours(2)))

        then:
        new GraphQLLocalDate(true).getCoercing().parseValue(value) == result

        where:
        value                  | result
        '2019-03-01'           | LocalDate.of(2019, 3, 1)
        '2019-03-01T22:00:00Z' | LocalDate.of(2019, 3, 2)
        '2019-03-01 22:00:00'  | LocalDate.of(2019, 3, 2)
    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        new GraphQLLocalDate().getCoercing().parseValue(value)
        then:
        thrown(CoercingParseValueException)

        where:
        value        | _
        ''           | _
        'not a date' | _
        new Object() | _
    }

}
