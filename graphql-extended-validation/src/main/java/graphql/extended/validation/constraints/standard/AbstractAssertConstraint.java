package graphql.extended.validation.constraints.standard;

import graphql.GraphQLError;
import graphql.extended.validation.constraints.AbstractDirectiveConstraint;
import graphql.extended.validation.rules.ValidationEnvironment;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLInputType;

import java.util.Collections;
import java.util.List;

import static graphql.Scalars.GraphQLBoolean;

abstract class AbstractAssertConstraint extends AbstractDirectiveConstraint {

    public AbstractAssertConstraint(String name) {
        super(name);
    }


    @Override
    public boolean appliesToType(GraphQLInputType inputType) {
        return isOneOfTheseTypes(inputType,
                GraphQLBoolean
        );
    }

    @Override
    protected List<GraphQLError> runConstraint(ValidationEnvironment validationEnvironment) {
        Object validatedValue = validationEnvironment.getValidatedValue();
        //null values are valid
        if (validatedValue == null) {
            return Collections.emptyList();
        }

        GraphQLDirective directive = validationEnvironment.getContextObject(GraphQLDirective.class);

        boolean isTrue = asBoolean(validatedValue);
        if (!isOK(isTrue)) {
            return mkError(validationEnvironment, directive, mkMessageParams(validatedValue, validationEnvironment));

        }
        return Collections.emptyList();
    }

    protected abstract boolean isOK(boolean isTrue);


}
