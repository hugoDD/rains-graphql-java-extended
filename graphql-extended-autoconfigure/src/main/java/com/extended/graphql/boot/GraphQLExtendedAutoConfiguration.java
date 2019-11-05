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
package com.extended.graphql.boot;

import com.extended.graphql.directive.validation.ValidationDirective;
import com.extended.graphql.scalars.datetime.*;
import com.extended.graphql.scalars.object.GraphQLObject;
import com.oembedler.moon.graphql.boot.GraphQLJavaToolsAutoConfiguration;
import com.oembedler.moon.graphql.boot.SchemaDirective;
import graphql.extended.validation.constraints.DirectiveConstraint;
import graphql.extended.validation.constraints.DirectiveConstraints;
import graphql.extended.validation.schemawiring.ValidationSchemaWiringHelp;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
@Configuration
@AutoConfigureBefore({GraphQLJavaToolsAutoConfiguration.class})
@EnableConfigurationProperties(GraphQLExtendedProperties.class)
public class GraphQLExtendedAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public List<SchemaDirective> validationSchemaDirective() {
        ValidationSchemaWiringHelp validationRun = ValidationSchemaWiringHelp.newInstance();
//        SchemaDirective sizeValidation = new SchemaDirective("Size",new ValidationDirective(validationRun));
//        SchemaDirective expressionValidation = new SchemaDirective("Expression",new ValidationDirective(validationRun));
//        SchemaDirective maxValidation = new SchemaDirective("Max",new ValidationDirective(validationRun));
//        SchemaDirective minValidation = new SchemaDirective("Min",new ValidationDirective(validationRun));
//        SchemaDirective negativeValidation = new SchemaDirective("Negative",new ValidationDirective(validationRun));
//        SchemaDirective negativeOrZeroValidation = new SchemaDirective("NegativeOrZero",new ValidationDirective(validationRun));
//        SchemaDirective notBlankValidation = new SchemaDirective("NotBlank",new ValidationDirective(validationRun));
//        SchemaDirective negativeValidation = new SchemaDirective("Negative",new ValidationDirective(validationRun));
//        SchemaDirective negativeValidation = new SchemaDirective("Negative",new ValidationDirective(validationRun));

        List<DirectiveConstraint> standardRules = new ArrayList<>(DirectiveConstraints.STANDARD_CONSTRAINTS);
        List<SchemaDirective> schemaDirectiveList = new ArrayList<>(standardRules.size());
        standardRules.forEach(s -> schemaDirectiveList.add(new SchemaDirective(s.getName(), new ValidationDirective(validationRun))));
        return schemaDirectiveList;
    }


    @Bean
    @ConditionalOnMissingBean(name = "graphQLTreeNode")
    public GraphQLObject graphQLTreeNode(GraphQLExtendedProperties configurationProperties) {
        return new GraphQLObject("TreeNode", "tree node children type");
    }

    @Bean
    @ConditionalOnMissingBean(name = "graphQLObject")
    public GraphQLObject graphQLObject(GraphQLExtendedProperties configurationProperties) {
        final String name = configurationProperties.getObject().getScalarName();
        final String pattern = configurationProperties.getDate().getPattern();
        if (name == null) {
            return new GraphQLObject();
        } else {
            return new GraphQLObject(name, pattern);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLDate graphQLDate(GraphQLExtendedProperties configurationProperties) {
        final String name = configurationProperties.getDate().getScalarName();
        final String pattern = configurationProperties.getDate().getPattern();
        if (name == null) {
            return new GraphQLDate();
        } else {
            return new GraphQLDate(name, pattern);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLLocalDate graphQLLocalDate(GraphQLExtendedProperties configurationProperties) {
        final String name = configurationProperties.getLocalDate().getScalarName();
        final String pattern = configurationProperties.getLocalDate().getPattern();
        if (name == null) {
            return new GraphQLLocalDate();
        } else {
            return new GraphQLLocalDate(name, pattern, false);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLLocalDateTime graphQLLocalDateTime(GraphQLExtendedProperties configurationProperties) {
        final String name = configurationProperties.getLocalDateTime().getScalarName();
        final String pattern = configurationProperties.getLocalDateTime().getPattern();
        if (name == null) {
            return new GraphQLLocalDateTime(configurationProperties.isZoneConversionEnabled());
        } else {
            return new GraphQLLocalDateTime(name, configurationProperties.isZoneConversionEnabled(), pattern);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLLocalTime graphQLLocalTime(GraphQLExtendedProperties configurationProperties) {
        final String name = configurationProperties.getLocalTime().getScalarName();
        final String pattern = configurationProperties.getLocalTime().getPattern();
        if (name == null) {
            return new GraphQLLocalTime();
        } else {
            return new GraphQLLocalTime(name, pattern);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLOffsetDateTime graphQLOffsetDateTime(GraphQLExtendedProperties configurationProperties) {
        final String name = configurationProperties.getOffsetDateTime().getScalarName();
        if (name == null) {
            return new GraphQLOffsetDateTime();
        } else {
            return new GraphQLOffsetDateTime(name);
        }
    }
}
