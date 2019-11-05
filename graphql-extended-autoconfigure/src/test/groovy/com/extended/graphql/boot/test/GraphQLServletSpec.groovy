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

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.StringEscapeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.*
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

/**
 * @author <ahref='mailto:280555235@qq.com' > hugoDD</a>
 */
//@EnableAutoConfiguration
//@SpringBootTest( webEnvironment = RANDOM_PORT)
class GraphQLServletSpec/* extends Specification */{

    @Shared
    ObjectMapper mapper = new ObjectMapper()

    @Autowired
    TestRestTemplate restTemplate

    def "test GraphQLServlet"() {
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
        String json = """
{
    "query": "${StringEscapeUtils.escapeJson(query)}"
}
"""

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)

        HttpEntity entity = new HttpEntity(json, headers)

        ResponseEntity<String> response = restTemplate.postForEntity('/graphql', entity, String.class)

        then:
        response.statusCode == HttpStatus.OK

        response.body
        getResponseContent(response.body) == [
                data: [
                        echo: [
                                date          : '2017-07-10 06:12:46',
                                localDate     : '2017-01-01',
                                localDateTime : '2017-01-01 00:00:00',
                                offsetDateTime: '2017-01-01T00:00:00Z',
                                localTime     : '00:00:00'
                        ]
                ]
        ]
    }

    private Map<String, Object> getResponseContent(String content) {
        mapper.readValue(content, Map)
    }

}
