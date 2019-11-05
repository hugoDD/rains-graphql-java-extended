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
package com.extended.graphql.scalars.object;

import graphql.Assert;
import graphql.Internal;
import graphql.language.*;
import graphql.schema.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.extended.graphql.scalars.util.Kit.typeName;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
@Internal
public class GraphQLObject extends GraphQLScalarType {
    private static final String DEFAULT_NAME = "Object";

    public GraphQLObject() {
        this(DEFAULT_NAME, "An object scalar");
    }

    public GraphQLObject(String name, String description) {
        super(name, description, new Coercing<Object, Object>() {
            @Override
            public Object serialize(Object input) throws CoercingSerializeException {
                return input;
            }

            @Override
            public Object parseValue(Object input) throws CoercingParseValueException {
                return input;
            }

            @Override
            public Object parseLiteral(Object input) throws CoercingParseLiteralException {
                return parseLiteral(input, Collections.emptyMap());
            }

            @Override
            public Object parseLiteral(Object input, Map<String, Object> variables) throws CoercingParseLiteralException {
                if (!(input instanceof Value)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                if (input instanceof NullValue) {
                    return null;
                }
                if (input instanceof FloatValue) {
                    return ((FloatValue) input).getValue();
                }
                if (input instanceof StringValue) {
                    return ((StringValue) input).getValue();
                }
                if (input instanceof IntValue) {
                    return ((IntValue) input).getValue();
                }
                if (input instanceof BooleanValue) {
                    return ((BooleanValue) input).isValue();
                }
                if (input instanceof EnumValue) {
                    return ((EnumValue) input).getName();
                }
                if (input instanceof VariableReference) {
                    String varName = ((VariableReference) input).getName();
                    return variables.get(varName);
                }
                if (input instanceof ArrayValue) {
                    List<Value> values = ((ArrayValue) input).getValues();
                    return values.stream()
                            .map(v -> parseLiteral(v, variables))
                            .collect(Collectors.toList());
                }
                if (input instanceof ObjectValue) {
                    List<ObjectField> values = ((ObjectValue) input).getObjectFields();
                    Map<String, Object> parsedValues = new LinkedHashMap<>();
                    values.forEach(fld -> {
                        Object parsedValue = parseLiteral(fld.getValue(), variables);
                        parsedValues.put(fld.getName(), parsedValue);
                    });
                    return parsedValues;
                }
                return Assert.assertShouldNeverHappen("We have covered all Value types");
            }
        });
    }

}
