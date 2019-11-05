package com.extended.graphql.directive.validation;

import graphql.extended.validation.schemawiring.ValidationSchemaWiringHelp;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

public class ValidationDirective implements SchemaDirectiveWiring {
    private ValidationSchemaWiringHelp validationSchemaWiringHelp;

    public ValidationDirective(ValidationSchemaWiringHelp validationSchemaWiringHelp) {
        this.validationSchemaWiringHelp = validationSchemaWiringHelp;
    }

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        validationSchemaWiringHelp.buildFieldDataFetch(environment);
        return environment.getElement();
    }

    @Override
    public GraphQLArgument onArgument(SchemaDirectiveWiringEnvironment<GraphQLArgument> environment) {
        validationSchemaWiringHelp.buildFieldDataFetch(environment);
        return environment.getElement();
    }

    @Override
    public GraphQLInputObjectType onInputObjectType(SchemaDirectiveWiringEnvironment<GraphQLInputObjectType> environment) {
        validationSchemaWiringHelp.buildFieldDataFetch(environment);
        return environment.getElement();
    }

    @Override
    public GraphQLInputObjectField onInputObjectField(SchemaDirectiveWiringEnvironment<GraphQLInputObjectField> environment) {
        validationSchemaWiringHelp.buildFieldDataFetch(environment);
        return environment.getElement();
    }
}
