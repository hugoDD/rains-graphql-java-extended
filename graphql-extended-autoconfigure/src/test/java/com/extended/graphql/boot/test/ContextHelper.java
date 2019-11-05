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
package com.extended.graphql.boot.test;

import com.extended.graphql.scalars.datetime.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oembedler.moon.graphql.boot.GraphQLJavaToolsAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
public class ContextHelper {

    static public AbstractApplicationContext load() {
        AbstractApplicationContext context;

        try {
            context = AnnotationConfigApplicationContext.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        AnnotationConfigRegistry registry = (AnnotationConfigRegistry) context;

        registry.register(BaseConfiguration.class);
        registry.register(GraphQLJavaToolsAutoConfiguration.class);

        context.refresh();

        return context;
    }

    @Configuration
    @ComponentScan("com.zhokhov.graphql.datetime.boot")
    static class BaseConfiguration {

        // initialize date time types here
        @Autowired(required = false)
        GraphQLDate graphQLDate;
        @Autowired(required = false)
        GraphQLLocalDate graphQLLocalDate;
        @Autowired(required = false)
        GraphQLLocalDateTime graphQLLocalDateTime;
        @Autowired(required = false)
        GraphQLLocalTime graphQLLocalTime;
        @Autowired(required = false)
        GraphQLOffsetDateTime graphQLOffsetDateTime;

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

            return mapper;
        }

    }

}
