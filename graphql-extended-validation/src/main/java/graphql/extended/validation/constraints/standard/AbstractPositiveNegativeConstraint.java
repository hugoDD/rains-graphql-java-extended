package graphql.extended.validation.constraints.standard;

import graphql.GraphQLError;
import graphql.Scalars;
import graphql.extended.validation.constraints.AbstractDirectiveConstraint;
import graphql.extended.validation.rules.ValidationEnvironment;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLScalarType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

abstract class AbstractPositiveNegativeConstraint extends AbstractDirectiveConstraint {

    public AbstractPositiveNegativeConstraint(String name) {
        super(name);
    }

    @Override
    public boolean appliesToType(GraphQLInputType inputType) {
        return isOneOfTheseTypes(inputType,
                Scalars.GraphQLByte,
                Scalars.GraphQLShort,
                Scalars.GraphQLInt,
                Scalars.GraphQLLong,
                Scalars.GraphQLBigDecimal,
                Scalars.GraphQLBigInteger,
                Scalars.GraphQLFloat
        );
    }

    public List<String> getApplicableTypeNames() {
        return Stream.of(Scalars.GraphQLByte,
                Scalars.GraphQLShort,
                Scalars.GraphQLInt,
                Scalars.GraphQLLong,
                Scalars.GraphQLBigDecimal,
                Scalars.GraphQLBigInteger,
                Scalars.GraphQLFloat)
                .map(GraphQLScalarType::getName)
                .collect(toList());
    }


    @Override
    protected List<GraphQLError> runConstraint(ValidationEnvironment validationEnvironment) {
        Object validatedValue = validationEnvironment.getValidatedValue();
        //null values are valid
        if (validatedValue == null) {
            return Collections.emptyList();
        }

        GraphQLDirective directive = validationEnvironment.getContextObject(GraphQLDirective.class);

        boolean isOK;
        try {
            BigDecimal bigDecimal = asBigDecimal(validatedValue);
            isOK = isOK(bigDecimal);
        } catch (NumberFormatException nfe) {
            isOK = false;
        }

        if (!isOK) {
            return mkError(validationEnvironment, directive, mkMessageParams(validatedValue, validationEnvironment));

        }
        return Collections.emptyList();
    }

    abstract protected boolean isOK(BigDecimal bigDecimal);
}
