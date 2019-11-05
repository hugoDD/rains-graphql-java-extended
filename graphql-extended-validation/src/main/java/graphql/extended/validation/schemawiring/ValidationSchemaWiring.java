package graphql.extended.validation.schemawiring;

import graphql.GraphQLError;
import graphql.PublicApi;
import graphql.extended.validation.interpolation.MessageInterpolator;
import graphql.extended.validation.rules.OnValidationErrorStrategy;
import graphql.extended.validation.rules.TargetedValidationRules;
import graphql.extended.validation.rules.ValidationRules;
import graphql.extended.validation.util.Util;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

import java.util.List;
import java.util.Locale;

/**
 * A {@link SchemaDirectiveWiring} that can be used to inject validation rules into the data fetchers
 * when the graphql schema is being built.  It will use the validation rules and ask each one of they apply to the field and or its
 * arguments.
 * <p>
 * If there are rules that apply then it will it will change the {@link DataFetcher} of that field so that rules get run
 * BEFORE the original field fetch is run.
 */
@PublicApi
public class ValidationSchemaWiring implements SchemaDirectiveWiring {

    private final ValidationRules ruleCandidates;

    public ValidationSchemaWiring(ValidationRules ruleCandidates) {
        this.ruleCandidates = ruleCandidates;
    }

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
        GraphQLFieldsContainer fieldsContainer = env.getFieldsContainer();
        GraphQLFieldDefinition fieldDefinition = env.getFieldDefinition();

        TargetedValidationRules rules = ruleCandidates.buildRulesFor(fieldDefinition, fieldsContainer);
        if (rules.isEmpty()) {
            return fieldDefinition; // no rules - no validation needed
        }

        OnValidationErrorStrategy errorStrategy = ruleCandidates.getOnValidationErrorStrategy();
        MessageInterpolator messageInterpolator = ruleCandidates.getMessageInterpolator();
        Locale locale = ruleCandidates.getLocale();

        final DataFetcher currentDF = env.getCodeRegistry().getDataFetcher(fieldsContainer, fieldDefinition);
        final DataFetcher newDF = buildValidatingDataFetcher(rules, errorStrategy, messageInterpolator, currentDF, locale);

        env.getCodeRegistry().dataFetcher(fieldsContainer, fieldDefinition, newDF);

        return fieldDefinition;
    }

    private DataFetcher buildValidatingDataFetcher(TargetedValidationRules rules, OnValidationErrorStrategy errorStrategy, MessageInterpolator messageInterpolator, DataFetcher currentDF, final Locale defaultLocale) {
        // ok we have some rules that need to be applied to this field and its arguments
        return environment -> {
            List<GraphQLError> errors = rules.runValidationRules(environment, messageInterpolator, defaultLocale);
            if (!errors.isEmpty()) {
                // should we continue?
                if (!errorStrategy.shouldContinue(errors, environment)) {
                    return errorStrategy.onErrorValue(errors, environment);
                }
            }
            // we have no validation errors or they said continue so call the underlying data fetcher
            Object returnValue = currentDF.get(environment);
            if (errors.isEmpty()) {
                return returnValue;
            }
            return Util.mkDFRFromFetchedResult(errors, returnValue);
        };
    }

}
