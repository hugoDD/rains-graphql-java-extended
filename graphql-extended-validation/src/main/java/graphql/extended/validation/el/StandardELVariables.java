package graphql.extended.validation.el;

import graphql.extended.validation.rules.ValidationEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;

import java.util.Map;

import static graphql.extended.validation.util.Util.mkMap;

public class StandardELVariables {

    public static Map<String, Object> standardELVars(ValidationEnvironment validationEnvironment) {
        GraphQLFieldDefinition fieldDefinition = validationEnvironment.getFieldDefinition();
        Object argument = validationEnvironment.getArgument();
        GraphQLFieldsContainer fieldsContainer = validationEnvironment.getFieldsContainer();
        Object validatedValue = validationEnvironment.getValidatedValue();
        Map<String, Object> argumentValues = validationEnvironment.getArgumentValues();

        return mkMap(
                "validatedValue", validatedValue,

                "gqlField", fieldDefinition,
                "gqlFieldContainer", fieldsContainer,

                "gqlArgument", argument,

                "args", argumentValues, // short hand
                "arguments", argumentValues
        );
    }
}
