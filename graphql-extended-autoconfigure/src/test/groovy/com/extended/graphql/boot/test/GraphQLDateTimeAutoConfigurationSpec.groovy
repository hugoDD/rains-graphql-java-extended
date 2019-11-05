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
package com.extended.graphql.boot.test


import com.extended.graphql.scalars.datetime.GraphQLLocalDate
import com.extended.graphql.scalars.datetime.GraphQLLocalDateTime
import com.extended.graphql.scalars.datetime.GraphQLLocalTime
import com.extended.graphql.scalars.datetime.GraphQLOffsetDateTime
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import org.springframework.context.support.AbstractApplicationContext
import spock.lang.Specification

/**
 * @author <ahref='mailto:280555235@qq.com' > hugoDD</a>
 */
class GraphQLDateTimeAutoConfigurationSpec /*extends Specification */{

    AbstractApplicationContext context

    def setup() {
        context = ContextHelper.load()
    }

    def cleanup() {
        if (context) {
            context.close()
            context = null
        }
    }

    def "test"() {
        given:
        String query = """
{
    echo {
        date
        localDate
        localDateTime
        localTime
        offsetDateTime
    }
}
"""

        when:
        this.context = ContextHelper.load()

        then:
        context.getBean(GraphQLSchema.class)
        context.getBean(com.extended.graphql.scalars.datetime.GraphQLDate.class)
        context.getBean(GraphQLLocalDate.class)
        context.getBean(GraphQLLocalDateTime.class)
        context.getBean(GraphQLLocalTime.class)
        context.getBean(GraphQLOffsetDateTime.class)

        when:
        GraphQL graphQL = GraphQL.newGraphQL(context.getBean(GraphQLSchema.class)).build()
        Map<String, Object> result = graphQL.execute(query).getData()

        then:
        result == [
                echo: [
                        date          : '2017-07-10 06:12:46',
                        localDate     : '2017-01-01',
                        localDateTime : '2017-01-01 00:00:00',
                        offsetDateTime: '2017-01-01T00:00:00Z',
                        localTime     : '00:00:00'
                ]
        ]
    }

}
