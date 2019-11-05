package graphql.extended.validation.interpolation;

import graphql.GraphQLError;
import graphql.PublicSpi;
import graphql.extended.validation.rules.ValidationEnvironment;

import java.util.Map;

/**
 * This is responsible for taking an message template and parameters
 * and turning it into a {@link GraphQLError}.
 * <p>
 * Remember error messages are allow to use Java EL expressions, like <pre>{@code ${formatter.format('%1$.2f', validatedValue)}}</pre> to build
 * more powerful error messages.
 */
@PublicSpi
public interface MessageInterpolator {
    /**
     * Called to interpolate a message template and arguments into a {@link GraphQLError}
     *
     * @param messageTemplate       the message template
     * @param messageParams         the parameters to this error
     * @param validationEnvironment the validation environment
     * @return a {@link GraphQLError}
     */
    GraphQLError interpolate(String messageTemplate, Map<String, Object> messageParams, ValidationEnvironment validationEnvironment);
}
