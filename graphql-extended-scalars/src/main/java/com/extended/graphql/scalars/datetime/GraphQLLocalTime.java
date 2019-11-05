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

import com.extended.graphql.scalars.util.DateTimeHelper;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
public class GraphQLLocalTime extends GraphQLScalarType {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME.withZone(ZoneOffset.UTC);
    private static final String DEFAULT_NAME = "LocalTime";

    public GraphQLLocalTime() {
        this(DEFAULT_NAME, null);
    }

    public GraphQLLocalTime(final String name, String pattern) {
        super(name, "Local Time type", new Coercing<LocalTime, String>() {
            private LocalTime convertImpl(Object input) {
                if (input instanceof String) {
                    try {
                        return LocalTime.parse((String) input, Objects.isNull(pattern) ? FORMATTER : DateTimeFormatter.ofPattern(pattern));
                    } catch (DateTimeParseException ignored) {
                    }
                }
                return null;
            }

            @Override
            public String serialize(Object input) {
                if (input instanceof LocalTime) {
                    return DateTimeHelper.toISOString((LocalTime) input, pattern);
                } else {
                    LocalTime result = convertImpl(input);
                    if (result == null) {
                        throw new CoercingSerializeException("Invalid value '" + input + "' for LocalTime");
                    }
                    return DateTimeHelper.toISOString(result, pattern);
                }
            }

            @Override
            public LocalTime parseValue(Object input) {
                LocalTime result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException("Invalid value '" + input + "' for LocalTime");
                }
                return result;
            }

            @Override
            public LocalTime parseLiteral(Object input) {
                if (!(input instanceof StringValue)) return null;
                String value = ((StringValue) input).getValue();
                LocalTime result = convertImpl(value);
                return result;
            }
        });
    }

}
