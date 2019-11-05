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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
public class GraphQLLocalDate extends GraphQLScalarType {

    private static final String DEFAULT_NAME = "LocalDate";

    public GraphQLLocalDate() {
        this(DEFAULT_NAME, null, false);
    }

    public GraphQLLocalDate(boolean zoneConversionEnabled) {
        this(DEFAULT_NAME, null, zoneConversionEnabled);
    }

    public GraphQLLocalDate(final String name, String pattern, boolean zoneConversionEnabled) {
        super(name, "Local Date type", new Coercing<LocalDate, String>() {

            private LocalDateTimeConverter converter = new LocalDateTimeConverter(zoneConversionEnabled);

            private LocalDate convertImpl(Object input) {
                if (input instanceof String) {
                    LocalDateTime localDateTime = converter.parseDate((String) input);

                    if (localDateTime != null) {
                        return localDateTime.toLocalDate();
                    }
                }
                return null;
            }

            @Override
            public String serialize(Object input) {
                if (input instanceof LocalDate) {
                    return DateTimeHelper.toISOString((LocalDate) input, pattern);
                } else {
                    LocalDate result = convertImpl(input);
                    if (result == null) {
                        throw new CoercingSerializeException("Invalid value '" + input + "' for LocalDate");
                    }
                    return DateTimeHelper.toISOString(result, pattern);
                }
            }

            @Override
            public LocalDate parseValue(Object input) {
                LocalDate result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException("Invalid value '" + input + "' for LocalDate");
                }
                return result;
            }

            @Override
            public LocalDate parseLiteral(Object input) {
                if (!(input instanceof StringValue)) return null;
                String value = ((StringValue) input).getValue();
                return convertImpl(value);
            }
        });
    }

}
