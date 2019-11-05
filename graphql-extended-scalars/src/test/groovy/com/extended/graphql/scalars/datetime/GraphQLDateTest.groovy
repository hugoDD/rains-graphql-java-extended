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

import static com.extended.graphql.scalars.util.DateTimeHelper.createDate

/**
 * @author <ahref='mailto:280555235@qq.com' > hugoDD</a>
 */
class GraphQLDateTest extends Specification {

    @Unroll
    def "Date parse literal #literal.value as #result"() {
        expect:
        new GraphQLDate().getCoercing().parseLiteral(literal) == result

        where:
        literal                                     | result
        new StringValue('2017-07-09T11:54:42.277Z') | createDate(2017, 7, 9, 11, 54, 42, 277)
        new StringValue('2017-07-09T13:14:45.947Z') | createDate(2017, 7, 9, 13, 14, 45, 947)
        new StringValue('2017-07-09T11:54:42Z')     | createDate(2017, 7, 9, 11, 54, 42)
        new StringValue('2017-07-09')               | createDate(2017, 7, 9)
        new StringValue('2017-07-09 11:30:30')      | createDate(2017, 7, 9, 11, 30, 30)
    }

    @Unroll
    def "Date returns null for invalid #literal"() {
        expect:
        new GraphQLDate().getCoercing().parseLiteral(literal) == null

        where:
        literal                       | _
        new StringValue("not a date") | _
    }

    @Unroll
    def "Date serialize #value into #result (#result.class)"() {
        expect:
        new GraphQLDate().getCoercing().serialize(value) == result

        where:
        value                                   | result
        createDate(2017, 7, 9, 11, 54, 42, 277) | '2017-07-09 11:54:42'
        createDate(2017, 7, 9, 13, 14, 45, 947) | '2017-07-09 13:14:45'
        createDate(2017, 7, 9, 11, 54, 42)      | '2017-07-09 11:54:42'
        createDate(2017, 7, 9)                  | '2017-07-09 00:00:00'
    }

    @Unroll
    def "serialize throws exception for invalid input #value"() {
        when:
        new GraphQLDate().getCoercing().serialize(value)
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
        new GraphQLDate().getCoercing().parseValue(value) == result

        where:
        value                      | result
        '2017-07-09T11:54:42.277Z' | createDate(2017, 7, 9, 11, 54, 42, 277)
        '2017-07-09T13:14:45.947Z' | createDate(2017, 7, 9, 13, 14, 45, 947)
        '2017-07-09T11:54:42Z'     | createDate(2017, 7, 9, 11, 54, 42)
        '2017-07-09'               | createDate(2017, 7, 9)
        '2017-07-09 11:54:42'      | createDate(2017, 7, 9, 11, 54, 42)
    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        new GraphQLDate().getCoercing().parseValue(value)
        then:
        thrown(CoercingParseValueException)

        where:
        value        | _
        ''           | _
        'not a date' | _
        new Object() | _
    }

}
