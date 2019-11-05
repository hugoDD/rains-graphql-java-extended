package com.extended.graphql.scalars.datetime;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GraphQLOffsetDateTime extends GraphQLScalarType {

    private static final String DEFAULT_NAME = "OffsetDateTime";

    public GraphQLOffsetDateTime() {
        this(DEFAULT_NAME);
    }

    public GraphQLOffsetDateTime(final String name) {
        super(name, "A Java OffsetDateTime", new Coercing<OffsetDateTime, String>() {
            private OffsetDateTime convertImpl(Object input) {
                if (input instanceof String) {
                    try {
                        return OffsetDateTime.parse((String) input, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    } catch (DateTimeParseException ignored) {
                        // nothing to-do
                    }
                }
                return null;
            }

            @Override
            public String serialize(Object input) {
                if (input instanceof OffsetDateTime) {
                    return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format((OffsetDateTime) input);
                } else {
                    OffsetDateTime result = convertImpl(input);
                    if (result == null) {
                        throw new CoercingSerializeException("Invalid value '" + input + "' for OffsetDateTime");
                    }
                    return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(result);
                }
            }

            @Override
            public OffsetDateTime parseValue(Object input) {
                OffsetDateTime result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException("Invalid value '" + input + "' for OffsetDateTime");
                }
                return result;
            }

            @Override
            public OffsetDateTime parseLiteral(Object input) {
                if (!(input instanceof StringValue)) {
                    return null;
                }
                String value = ((StringValue) input).getValue();
                return convertImpl(value);
            }
        });
    }

}
